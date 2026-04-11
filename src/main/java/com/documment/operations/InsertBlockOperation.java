package com.documment.operations;

import com.documment.domain.DocumentElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Operation to insert a new block into the document.
 */
public class InsertBlockOperation extends AbstractOperation {
    private final int index;
    private final DocumentElement element;

    public InsertBlockOperation(String userId, long baseVersion, int index, DocumentElement element) {
        super(userId, baseVersion);
        this.index = index;
        this.element = element;
    }

    public int getIndex() {
        return index;
    }

    public DocumentElement getElement() {
        return element;
    }

    @Override
    public List<DocumentElement> apply(List<DocumentElement> elements) {
        List<DocumentElement> newElements = new ArrayList<>(elements);
        int targetIndex = Math.min(index, newElements.size());
        newElements.add(targetIndex, element.copy());
        return newElements;
    }

    @Override
    public Operation transform(Operation other) {
        if (other instanceof InsertBlockOperation) {
            InsertBlockOperation otherInsert = (InsertBlockOperation) other;
            if (otherInsert.getIndex() < this.index || 
                (otherInsert.getIndex() == this.index && otherInsert.getUserId().compareTo(this.getUserId()) < 0)) {
                // If the other insert happened before or at the same position with higher priority (lexicographical user ID)
                return new InsertBlockOperation(getUserId(), getBaseVersion() + 1, index + 1, element);
            }
        } else if (other instanceof DeleteBlockOperation) {
            DeleteBlockOperation otherDelete = (DeleteBlockOperation) other;
            if (otherDelete.getIndex() < this.index) {
                return new InsertBlockOperation(getUserId(), getBaseVersion() + 1, index - 1, element);
            }
        }
        return new InsertBlockOperation(getUserId(), getBaseVersion() + 1, index, element);
    }
}
