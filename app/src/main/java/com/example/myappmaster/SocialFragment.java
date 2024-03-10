package com.example.myappmaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {

    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private Button postButton;
    private DatabaseReference databaseReference;
    private ImageView profilePictureImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        // Initialize profile picture ImageView
        profilePictureImageView = view.findViewById(R.id.profilePicture);



        // Initialize RecyclerView
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize post list (you need to populate it with data)
        postList = new ArrayList<>();

        // Initialize adapter and set it to RecyclerView
        postAdapter = new PostAdapter(postList);
        postsRecyclerView.setAdapter(postAdapter);

        // Initialize views for adding new posts
        postButton = view.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        // Set up the "Edit Profile" button click listener
        Button editProfileButton = view.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        return view;
    }

    private void addPost() {
        // Generate unique key for the post
        String postId = databaseReference.push().getKey();
        // Create new Post object
        Post post = new Post("", 0); // Empty content for now, you can modify this
        // Add post to Firebase
        databaseReference.child(postId).setValue(post);
        // Show success message
        Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
    }


    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

        private List<Post> postList;

        public PostAdapter(List<Post> postList) {
            this.postList = postList;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            Post post = postList.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {

            private TextView postContent;
            private TextView likesCount;
            private EditText commentInput;
            private ImageView postCommentIcon;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                postContent = itemView.findViewById(R.id.postImage);
                likesCount = itemView.findViewById(R.id.likesCount);
                commentInput = itemView.findViewById(R.id.commentInput);
                postCommentIcon = itemView.findViewById(R.id.postCommentIcon);

                postCommentIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle click event for postCommentIcon
                        // For example, open a comment dialog or perform other actions
                    }
                });
            }

            public void bind(Post post) {
                postContent.setText(post.getContent());
                likesCount.setText(post.getLikesCount() + " likes");
                // You can bind more data if needed
            }
        }
    }

    // Define the Post class here
    private static class Post {
        private String content;
        private int likesCount;

        public Post(String content, int likesCount) {
            this.content = content;
            this.likesCount = likesCount;
        }

        public String getContent() {
            return content;
        }

        public int getLikesCount() {
            return likesCount;
        }
    }
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;

    // Initialize Firebase Storage reference
    private StorageReference storageReference;




    // Method to open the image chooser intent
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image chooser intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadProfilePicture(imageUri);
        }
    }

    // Method to upload the selected profile picture to Firebase Storage
    private void uploadProfilePicture(Uri imageUri) {
        // Define the path for the profile picture in Firebase Storage
        StorageReference profilePicsRef = storageReference.child("profile_pictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Upload the image to Firebase Storage
        profilePicsRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Profile picture upload successful
                    // Get the download URL of the uploaded image
                    profilePicsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the profile picture ImageView with the downloaded image URL
                        String imageUrl = uri.toString();
                        // Assuming you have an ImageView for the profile picture with ID profilePictureImageView
                        profilePictureImageView.setImageURI(imageUri);
                        // You can also use a library like Glide or Picasso to load the image
                        // Glide.with(getContext()).load(imageUrl).into(profilePictureImageView);
                        // Save the image URL to Firestore or Realtime Database for future use (Optional)
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during upload
                    Toast.makeText(getContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                });
    }


}


