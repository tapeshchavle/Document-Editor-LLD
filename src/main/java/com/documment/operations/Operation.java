package com.documment.operations;

import com.documment.domain.DocumentElement;
import java.util.List;

/**
 * Interface for all operations that can be performed on a document.
 * Each operation knows how to apply itself to the document state.
 */
public interface Operation {
    String getUserId();
    long getBaseVersion();
    
    /**
     * Applies this operation to the given list of document elements.
     * 
     * @param elements The current state of the document elements.
     * @return The new state of the document elements.
     */
    List<DocumentElement> apply(List<DocumentElement> elements);
    
    /**
     * Transforms this operation against another operation that happened concurrently.
     * This is the heart of Operational Transformation (OT).
     * 
     * @param other The concurrent operation to transform against.
     * @return A new transformed operation.
     */
    Operation transform(Operation other);
}
