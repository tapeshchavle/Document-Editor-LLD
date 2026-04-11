package com.documment.domain;

/**
 * Base interface for all components within a document.
 * Follows the Open/Closed Principle: new element types (Video, Table) 
 * can be added by implementing this interface without changing the core engine.
 */
public interface DocumentElement {
    String getId();
    String getType();
    
    // Deep copy for immutability during transformations
    DocumentElement copy();
}
