package com.documment.repository;

import com.documment.domain.DocumentElement;
import java.util.List;

/**
 * Interface for storing document snapshots.
 */
public interface DocumentSnapshotStore {
    
    /**
     * Retrieves the current full state of the document.
     */
    List<DocumentElement> getDocumentState(String documentId);
    
    /**
     * Updates the document state.
     */
    void updateDocumentState(String documentId, List<DocumentElement> newState);
}
