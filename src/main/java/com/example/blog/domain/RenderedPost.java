package com.example.blog.domain;

public class RenderedPost {

    private final PostMeta meta;
    private final String html;

    public RenderedPost(PostMeta meta, String html) {
        this.meta = meta;
        this.html = html;
    }

    public PostMeta getMeta() {
        return meta;
    }

    public String getHtml() {
        return html;
    }
}
