package com.documment.repository;

import com.documment.operations.Operation;
import java.util.List;

/**
 * Interface for storing and retrieving operations (Event Sourcing).
 * In a real production environment, this would be backed by a high-throughput 
 * database like PostgreSQL or a distributed log like Kafka.
 */
public interface OperationStore {
    
    /**
     * Appends an operation to the document's history.
     */
    void save(String documentId, Operation operation);
    
    /**
     * Retrieves operations that occurred after a specific version.
     */
    List<Operation> getHistory(String documentId, long sinceVersion);
    
    /**
     * Gets the latest version number for a document.
     */
    long getLatestVersion(String documentId);
}
