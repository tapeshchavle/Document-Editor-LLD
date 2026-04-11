package com.documment.service;

import com.documment.domain.DocumentElement;
import com.documment.engine.OTEngine;
import com.documment.messaging.MessageBroadcaster;
import com.documment.operations.Operation;
import com.documment.repository.DocumentSnapshotStore;
import com.documment.repository.OperationStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Orchestrates real-time collaboration logic.
 * Ensures data consistency using Operational Transformation.
 */
@Service
public class CollaborationService {

    private final OperationStore operationStore;
    private final DocumentSnapshotStore snapshotStore;
    private final OTEngine otEngine;
    private final MessageBroadcaster broadcaster;
    
    // Simple locking per document to ensure sequential processing on the server
    private final java.util.Map<String, ReentrantLock> documentLocks = new java.util.concurrent.ConcurrentHashMap<>();

    public CollaborationService(OperationStore operationStore, 
                                DocumentSnapshotStore snapshotStore, 
                                OTEngine otEngine, 
                                MessageBroadcaster broadcaster) {
        this.operationStore = operationStore;
        this.snapshotStore = snapshotStore;
        this.otEngine = otEngine;
        this.broadcaster = broadcaster;
    }

    /**
     * Processes an incoming operation from a client.
     */
    public void handleOperation(String documentId, Operation incoming) {
        ReentrantLock lock = documentLocks.computeIfAbsent(documentId, k -> new ReentrantLock());
        lock.lock();
        try {
            // 1. Get history since the incoming operation's base version
            List<Operation> history = operationStore.getHistory(documentId, incoming.getBaseVersion());
            
            // 2. Transform the incoming operation against concurrent history
            Operation transformed = otEngine.transform(incoming, history);
            
            if (transformed != null) {
                // 3. Apply the transformed operation to the current state
                List<DocumentElement> currentState = snapshotStore.getDocumentState(documentId);
                List<DocumentElement> newState = transformed.apply(currentState);
                
                // 4. Persist the operation and the new state
                operationStore.save(documentId, transformed);
                snapshotStore.updateDocumentState(documentId, newState);
                
                // 5. Broadcast transformed op to all collaborators
                broadcaster.broadcast(documentId, transformed);
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Gets the full state of the document for a new client.
     */
    public List<DocumentElement> getDocumentState(String documentId) {
        return snapshotStore.getDocumentState(documentId);
    }
}
