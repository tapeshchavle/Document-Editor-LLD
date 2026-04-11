package com.documment.messaging;

import com.documment.operations.Operation;

/**
 * Interface for broadcasting document updates to connected clients.
 */
public interface MessageBroadcaster {
    
    /**
     * Broadcasts a transformed operation to all clients collaborating on a document.
     */
    void broadcast(String documentId, Operation operation);
}
