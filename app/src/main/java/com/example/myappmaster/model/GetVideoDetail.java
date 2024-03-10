package com.example.myappmaster.model;

import com.google.gson.annotations.SerializedName;

public class GetVideoDetail {
    @SerializedName("id")
    private String id;

    @SerializedName("snippet")
    private Snippet snippet;


    public String getVideoID() {
        return this.id;
    }

    public String getVideoTitle() {
        return this.snippet.getTitle();
    }

    public String getVideoDescription() {
        return this.snippet.getDescription();
    }

    public String getThumbnailURL() {
        return this.snippet.getThumbnailUrl();
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
