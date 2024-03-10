package com.example.myappmaster.model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YouTubeApiClient {
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String API_KEY = "KEY";

    private YouTubeApiService apiService;

    public YouTubeApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(YouTubeApiService.class);
    }

    public void searchVideos(String query, Callback<List<Video>> callback) {
        Call<SearchVideosResponse> call = apiService.searchVideos("snippet", query, 20, API_KEY);
        call.enqueue(new retrofit2.Callback<SearchVideosResponse>() {
            @Override
            public void onResponse(Call<SearchVideosResponse> call, retrofit2.Response<SearchVideosResponse> response) {
                if (response.isSuccessful()) {
                    List<Video> result = new ArrayList<Video>();
                    for (SeachVideoDetail video : response.body().getItems()) {
                        if (video.getVideoKind().equals("youtube#video")) {
                            result.add(new Video(video.getVideoId(), video.getTitle(), video.getDescription(), video.getThumbnailUrl()));
                        }
                    }
                    callback.onSuccess(result);
                } else {
                    callback.onError(new Exception("Failed to fetch videos"));
                }
            }

            @Override
            public void onFailure(Call<SearchVideosResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getVideos(List<String> videoIds, Callback<List<Video>> callback) {
        Call<GetVideosResponse> call = apiService.getVideos("snippet", String.join(",", videoIds), API_KEY);
        call.enqueue(new retrofit2.Callback<GetVideosResponse>() {
            @Override
            public void onResponse(Call<GetVideosResponse> call, retrofit2.Response<GetVideosResponse> response) {
                if (response.isSuccessful()) {
                    List<Video> result = new ArrayList<Video>();
                    for (GetVideoDetail video : response.body().getItems()) {
                        result.add(new Video(video.getVideoID(), video.getVideoTitle(), video.getVideoDescription().substring(0, Math.min(video.getVideoDescription().length(), 100)) + " ...", video.getThumbnailURL()));
                    }
                    callback.onSuccess(result);
                } else {
                    callback.onError(new Exception("Failed to fetch videos"));
                }
            }

            @Override
            public void onFailure(Call<GetVideosResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Throwable t);
    }
}
