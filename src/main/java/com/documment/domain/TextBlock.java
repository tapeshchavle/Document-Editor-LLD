package com.documment.domain;

import java.util.UUID;

/**
 * Represents a block of text in the document.
 */
public class TextBlock implements DocumentElement {
    private final String id;
    private String content;

    public TextBlock(String content) {
        this(UUID.randomUUID().toString(), content);
    }

    public TextBlock(String id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public DocumentElement copy() {
        return new TextBlock(this.id, this.content);
    }
}
