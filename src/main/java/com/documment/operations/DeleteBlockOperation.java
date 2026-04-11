package com.documment.operations;

import com.documment.domain.DocumentElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Operation to delete a block at a specific index.
 */
public class DeleteBlockOperation extends AbstractOperation {
    private final int index;

    public DeleteBlockOperation(String userId, long baseVersion, int index) {
        super(userId, baseVersion);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public List<DocumentElement> apply(List<DocumentElement> elements) {
        List<DocumentElement> newElements = new ArrayList<>(elements);
        if (index >= 0 && index < newElements.size()) {
            newElements.remove(index);
        }
        return newElements;
    }

    @Override
    public Operation transform(Operation other) {
        if (other instanceof InsertBlockOperation) {
            InsertBlockOperation otherInsert = (InsertBlockOperation) other;
            if (otherInsert.getIndex() <= this.index) {
                return new DeleteBlockOperation(getUserId(), getBaseVersion() + 1, index + 1);
            }
        } else if (other instanceof DeleteBlockOperation) {
            DeleteBlockOperation otherDelete = (DeleteBlockOperation) other;
            if (otherDelete.getIndex() < this.index) {
                return new DeleteBlockOperation(getUserId(), getBaseVersion() + 1, index - 1);
            } else if (otherDelete.getIndex() == this.index) {
                // Both deleting the same item. Return a No-Op.
                return new NoOpOperation(getUserId(), getBaseVersion() + 1);
            }
        }
        return new DeleteBlockOperation(getUserId(), getBaseVersion() + 1, index);
    }
}
