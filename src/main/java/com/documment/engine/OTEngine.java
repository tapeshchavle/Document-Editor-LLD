package com.documment.engine;

import com.documment.operations.Operation;
import java.util.List;

/**
 * Interface for the Operational Transformation Engine.
 * Responsible for resolving concurrency conflicts.
 */
public interface OTEngine {
    
    /**
     * Transforms an incoming operation against a list of concurrent operations
     * that have already been applied to the document.
     * 
     * @param incoming The operation sent by a client.
     * @param history The list of operations that happened after the incoming operation's base version.
     * @return The transformed operation relative to the current document state.
     */
    Operation transform(Operation incoming, List<Operation> history);
}
