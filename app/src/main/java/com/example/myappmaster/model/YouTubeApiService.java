package com.example.myappmaster.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface YouTubeApiService {
    @GET("search")
    Call<SearchVideosResponse> searchVideos(
            @Query("part") String part,
            @Query("q") String query,
            @Query("maxResults") int maxResults,
            @Query("key") String apiKey
    );

    @GET("videos")
    Call<GetVideosResponse> getVideos(
            @Query("part") String part,
            @Query("id") String videoIds,
            @Query("key") String apiKey
    );
}
