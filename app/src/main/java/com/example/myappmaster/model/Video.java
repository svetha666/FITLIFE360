package com.example.myappmaster.model;

public class Video {
    private String id;

    private String type;

    private boolean isLiked = false;

    private String title;
    private String description;
    private String thumbnailURL;

    public Video(String id, String title, String description, String thumbnailURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void toggleIsLiked() {
        isLiked = !isLiked;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
