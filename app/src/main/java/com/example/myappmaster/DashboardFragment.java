package com.example.myappmaster;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappmaster.model.Video;
import com.example.myappmaster.model.VideoAdapter;
import com.example.myappmaster.model.YouTubeApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;
    private YouTubeApiClient youTubeApiClient;
    private Button dietButton;
    private Button upperBodyWorkoutButton;
    private Button lowerBodyWorkoutButton;
    private Button coreWorkoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dietButton=view.findViewById(R.id.diet_button);
        upperBodyWorkoutButton=view.findViewById(R.id.upper_body_workout_button);
        lowerBodyWorkoutButton=view.findViewById(R.id.lower_body_workout_button);
        coreWorkoutButton=view.findViewById(R.id.core_workout_button);

        dietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchVideoData("diet", VideoType.DIET);
            }
        });

        upperBodyWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchVideoData("upper body workout", VideoType.WORKOUT);
            }
        });

        lowerBodyWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchVideoData("lower body workout", VideoType.WORKOUT);
            }
        });

        coreWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchVideoData("core workout", VideoType.WORKOUT);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(videoAdapter);

        youTubeApiClient = new YouTubeApiClient();

        // Fetch and populate video data
        fetchVideoData("upper body workout", VideoType.WORKOUT);

        return view;
    }



    private void fetchVideoData(String query, VideoType type) {
        // Simulate fetching video data from a remote source
        // For demonstration purposes, we'll create a list of sample YouTube videos
       /* List<YouTubeVideo> videos = new ArrayList<>();

        // Add sample YouTube videos
        videos.add(new YouTubeVideo("videoId1", "Title 1", "Description 1", "https://example.com/thumbnail1.jpg"));
        videos.add(new YouTubeVideo("videoId2", "Title 2", "Description 2", "https://example.com/thumbnail2.jpg"));
        // Add more videos as needed*/
        Context context = this.getContext();
        youTubeApiClient.searchVideos(query, new YouTubeApiClient.Callback<List<Video>>() {
            @Override
            public void onSuccess(List<Video> result) {
                for (Video video : result) {
                    video.setType(type.toString());
                }
                UserService.likedVideos(context, type.toString(), new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot likedVideo: snapshot.getChildren()){
                            for (Video video : result) {
                                if (video.getId().equals(likedVideo.getValue().toString())) {
                                    video.toggleIsLiked();
                                    break;
                                }
                            }
                        }

                        // Update the videoList with the fetched data
                        videoList.clear(); // Clear the existing list
                        videoList.addAll(result); // Add the fetched videos to the list

                        // Notify the adapter of the data change
                        videoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onError(Throwable t) {
                // Handle the error here
                Log.d("Dashboard Fragment", "error");
            }

            // Method to simulate fetching video data (replace it with your actual implementation)
//            private List<YouTubeVideo> getVideoList() {
//                // Simulated list of YouTube videos
//                List<YouTubeVideo> videos = new ArrayList<>();
//                videos.add(new YouTubeVideo("videoId1", "Title 1", "Description 1", "https://example.com/thumbnail1.jpg"));
//                videos.add(new YouTubeVideo("videoId2", "Title 2", "Description 2", "https://example.com/thumbnail2.jpg"));
//                // Add more videos as needed
//                return videos;
//            }
        });
    }
}