package com.example.myappmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideosResponse {
    @SerializedName("items")
    private List<GetVideoDetail> items;

    public List<GetVideoDetail> getItems() {
        return items;
    }
}
