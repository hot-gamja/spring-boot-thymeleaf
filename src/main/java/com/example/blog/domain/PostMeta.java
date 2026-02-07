package com.example.blog.domain;

import java.time.LocalDate;
import java.util.List;

public class PostMeta {

    private String slug;
    private String title;
    private LocalDate date;
    private List<String> tags;
    private String category;
    private String summary;
    private String description;
    private String thumbnailUrl;

    public PostMeta() {
    }

    public PostMeta(String slug, String title, LocalDate date, List<String> tags,
                    String category, String summary, String description, String thumbnailUrl) {
        this.slug = slug;
        this.title = title;
        this.date = date;
        this.tags = tags;
        this.category = category;
        this.summary = summary;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "PostMeta{slug='" + slug + "', title='" + title + "', date=" + date + ", category='" + category + "'}";
    }
}
