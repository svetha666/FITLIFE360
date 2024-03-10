package com.example.myappmaster;

import android.content.SharedPreferences;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserService {
//    private static DatabaseReference userReference;
    private static final String SHARED_PREF_NAME = "user_session";
    private static final String KEY_USERNAME = "username";

    private static DatabaseReference getDBReference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Get SharedPreferences editor to modify data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String user = sharedPreferences.getString(KEY_USERNAME, null);
        return FirebaseDatabase.getInstance().getReference("users").child(user);
    }
    public static void login(Context context, String user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Get SharedPreferences editor to modify data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USERNAME, user);
        editor.commit();
    }

    public static void likeVideo(Context context, String type, String videoId) {
        UserService.getDBReference(context).child("liked_" + type).push().setValue(videoId);
    }
    public static void updateProfile(Context context, String name, int age, float weight, float height) {
        DatabaseReference dbReference = UserService.getDBReference(context);
        dbReference.child("name").setValue(name);
        dbReference.child("age").setValue(age);
        dbReference.child("weight").setValue(weight);
        dbReference.child("height").setValue(height);
    }

    public static void getProfile(Context context, ValueEventListener listener) {
        DatabaseReference dbReference = UserService.getDBReference(context);
        dbReference.addListenerForSingleValueEvent(listener);
    }

    public static void unlikeVideo(Context context, String type, String videoId) {
        UserService.getDBReference(context).child("liked_" + type).addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot likedVideo: snapshot.getChildren()){
                    if (likedVideo.getValue().toString().equals(videoId)) {
                        UserService.getDBReference(context).child("liked_" + type).child(likedVideo.getKey()).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void likedVideos(Context context, String type, ValueEventListener listener) {
        UserService.getDBReference(context).child("liked_" + type).addListenerForSingleValueEvent(listener);
    }
    public static void logout(Context context) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Get SharedPreferences editor to modify data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the stored username
        editor.remove(KEY_USERNAME);

        // Apply changes
        editor.apply();
    }
}
