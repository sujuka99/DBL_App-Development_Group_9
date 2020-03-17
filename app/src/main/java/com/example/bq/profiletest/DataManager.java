package com.example.bq.profiletest;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class DataManager {

    public static DataManager instance;

    private DataManager(){}

    public void loadUserData(final String id, final DataChangeObserver observer){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(id);

        // We get notified once the data is loaded the first time
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Log.d("Database:", "DataSnapshot not found!");
                }

                UserData data = dataSnapshot.getValue(UserData.class);

                observer.notifyOfDataChange(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("setProfilePicture", "Failed due to an: " +
                        databaseError.getMessage());
            }
        });
    }

    public void updateUserData(@NonNull String id, @NonNull UserData data){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(id);

        userRef.setValue(data);
    }

    /**
     * Download the profile picture of the desired user and return the location of where it's stored
     * for reference.
     * @param id - ID of the desired user
     * @return - Uri which points to the image location
     */
    public void loadProfilePicture(final String id, final DataChangeObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the user it's profile picture
        // Which is stored as "users/<ID>/profile.jpg"
        StorageReference storageRef = ref.child("users/" + id + "/profile.jpg");

        // Attempt to download the image and get the location Uri
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // If the image was found, we notify the requester of the result
                observer.notifyOfDataChange(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // If the image was not found/something was wrong, we attempt to load the default
                // profile picture
                loadDefaultPicture(observer);
            }
        });
    }

    /**
     * Download the default profile picture and return the location of where it's stored
     * for reference.
     * @return - Uri which points to the image location
     */
    private void loadDefaultPicture(final DataChangeObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the default profile picture
        // Which is stored as "default/profile.jpg"
        StorageReference storageRef = ref.child("default/profile.jpg");

        // Attempt to download the image and get the location Uri
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // If the image was found, set the result to the found Uri
                observer.notifyOfDataChange(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // If failed, something went wrong and there isn't really much
                // we can do...
                Log.d("getDefaultProfilePicture: ", "Failed due to an: " +
                        exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    /**
     * Upload a profile picture to the firebase storage for a user
     * @param id - The ID of the desired user
     * @param path - The Uri pointer to the picture to be uploaded
     */
    public void setProfilePicture(@NonNull String id, @NonNull Uri path){
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference storageRef = ref.child("users/" + id +"/profile.jpg");

        storageRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("setProfilePicture: ", "Failed due to an: " +
                        exception.getMessage());
                exception.printStackTrace();
            }
        });
    }
    public static DataManager getInstance(){
        return instance == null ? (instance = new DataManager()) : instance;
    }
}
