package com.example.myappmaster.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchVideosResponse {
    @SerializedName("items")
    private List<SeachVideoDetail> items;

    public List<SeachVideoDetail> getItems() {
        return items;
    }
}

