package com.documment.operations;

import com.documment.domain.DocumentElement;
import java.util.List;

/**
 * An operation that does nothing. Useful for resolving conflicts 
 * where an operation becomes redundant.
 */
public class NoOpOperation extends AbstractOperation {

    public NoOpOperation(String userId, long baseVersion) {
        super(userId, baseVersion);
    }

    @Override
    public List<DocumentElement> apply(List<DocumentElement> elements) {
        return elements; // No changes
    }

    @Override
    public Operation transform(Operation other) {
        return new NoOpOperation(getUserId(), getBaseVersion() + 1);
    }
}
