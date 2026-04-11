package com.documment.operations;

/**
 * Base class for operations to reduce boilerplate.
 */
public abstract class AbstractOperation implements Operation {
    private final String userId;
    private final long baseVersion;

    protected AbstractOperation(String userId, long baseVersion) {
        this.userId = userId;
        this.baseVersion = baseVersion;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public long getBaseVersion() {
        return baseVersion;
    }
}
