package com.example.myappmaster.model;

import com.google.gson.annotations.SerializedName;

public class SeachVideoDetail {
    @SerializedName("id")
    private VideoId id;

    @SerializedName("snippet")
    private Snippet snippet;

    public String getVideoId() {
        return id.getVideoId();
    }

    public String getVideoKind() {
        return id.getKind();
    }

    public String getTitle() {
        return snippet.getTitle();
    }

    public String getDescription() {
        return snippet.getDescription();
    }

    public String getThumbnailUrl() {
        return snippet.getThumbnailUrl();
    }

    private class VideoId {
        @SerializedName("kind")
        private String kind;

        @SerializedName("videoId")
        private String videoId;

        public String getVideoId() {
            return videoId;
        }

        public String getKind() {
            return kind;
        }
    }

    private class Snippet {
        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("thumbnails")
        private Thumbnails thumbnails;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getThumbnailUrl() {
            return thumbnails.getHighRes().getUrl();
        }

    }

    private class Thumbnails {
        @SerializedName("default")
        private Thumbnail defaultThumbnail;

        @SerializedName("medium")
        private Thumbnail mediumResThumbnail;

        @SerializedName("high")
        private Thumbnail highResThumbnail;

        public Thumbnail getDefault() {
            return defaultThumbnail;
        }

        public Thumbnail getMediumRes() {
            return mediumResThumbnail;
        }

        public Thumbnail getHighRes() {
            return highResThumbnail;
        }
    }

    private class Thumbnail {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
