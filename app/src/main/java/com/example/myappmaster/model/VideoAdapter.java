package com.example.myappmaster.model;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappmaster.R;
import com.example.myappmaster.UserService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> videoList;

    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());
        holder.descriptionTextView.setText(video.getDescription());
        Picasso.get().load(video.getThumbnailURL()).into(holder.thumbnailImageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getId()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + video.getId()));
                try {
                    v.getContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    v.getContext().startActivity(webIntent);
                }
            }
        });
        if (video.isLiked()) {
            holder.videoLikeButton.setColorFilter(Color.argb(255, 222, 163, 41));
        } else {
            holder.videoLikeButton.setColorFilter(Color.argb(255, 255, 255, 255));
        }
        holder.videoLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isLiked()) {
                    UserService.unlikeVideo(v.getContext(), video.getType().toString(), video.getId());
                } else {
                    UserService.likeVideo(v.getContext(), video.getType().toString(), video.getId());
                }
                video.toggleIsLiked();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView thumbnailImageView;
        public CardView cardView;
        public ImageButton videoLikeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            cardView = itemView.findViewById(R.id.video_card);
            videoLikeButton = itemView.findViewById(R.id.video_like_button);
        }
    }
}
