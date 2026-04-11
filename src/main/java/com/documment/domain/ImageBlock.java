package com.documment.domain;

import java.util.UUID;

/**
 * Represents an image element in the document.
 */
public class ImageBlock implements DocumentElement {
    private final String id;
    private String url;
    private String caption;

    public ImageBlock(String url, String caption) {
        this(UUID.randomUUID().toString(), url, caption);
    }

    public ImageBlock(String id, String url, String caption) {
        this.id = id;
        this.url = url;
        this.caption = caption;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public DocumentElement copy() {
        return new ImageBlock(this.id, this.url, this.caption);
    }
}
