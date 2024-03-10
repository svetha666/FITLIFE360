package com.example.myappmaster;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikedVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedVideoFragment extends Fragment {

    public static final String TYPE = "type";

    private String type;
    private RecyclerView recyclerView;
    private YouTubeApiClient youTubeApiClient;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;

    public LikedVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked_videos, container, false);

        youTubeApiClient = new YouTubeApiClient();
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoList);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);

        Context context = this.getContext();
        UserService.likedVideos(context, type, new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> videoIDs = new ArrayList<String>();
                for(DataSnapshot likedVideo: snapshot.getChildren()){
                    videoIDs.add(likedVideo.getValue().toString());
                }
                youTubeApiClient.getVideos(videoIDs, new YouTubeApiClient.Callback<List<Video>>() {

                    @Override
                    public void onSuccess(List<Video> result) {
                        for (Video video : result) {
                            video.setType(type);
                            video.toggleIsLiked();
                        }
                        // Update the videoList with the fetched data
                        videoList.clear(); // Clear the existing list
                        videoList.addAll(result); // Add the fetched videos to the list

                        // Notify the adapter of the data change
                        videoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("LikedVideos", "error");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}