package com.example.bq.profiletest;

import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bq.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

/**
 * ProfileManager is a /static/ class that will take care of all interactions related to user
 * profile data.
 * It offers functionality to load and edit profile data for users.
 */
public class ProfileManager {

    private static ProfileManager instance;

    private ProfileManager() {}

    // This refers to the root of the database
    StorageReference ref = FirebaseStorage.getInstance().getReference();

    public void test() {
        StorageReference img = ref.child("test/BandQImg.jpeg");
        UploadTask task = img.putFile(Uri.fromFile(new File("../")));
    }

    /**
     * Get the {@link ProfileData} for the specified user
     * @pre {id != null && \exists(id)}
     * @post {data != null}
     * @param id - Unique identifier of the user
     * @return An instance of {@link ProfileData} containing all data found for the id
     */
    public ProfileData getProfileData(@NonNull UUID id) {
        ProfileData data = new ProfileData();

        // Download the image from the Firebase storage
        data.profilePicture = getProfilePicture(id);

        // Get the other profile parameters from the database
        data.username = "Test";
        data.biography = "This is a test";
        data.university = "Tu/e";
        data.study = "Computer Science";

        return data;
    }

    /**
     * Update the {@link ProfileData} for the specified user in Firebase
     * Main usage is meant for fully filled in {@link ProfileData} object with no null fields, even
     * if a field is not updated, but will ignore null fields while updating.
     * @param id - The ID of the user
     * @param data - The profile data to be stored in firebase
     */
    public void setProfileData(@NonNull UUID id, @NonNull ProfileData data){
        if(data.profilePicture != null){
            setProfilePicture(id, data.profilePicture);
        }

    }

    /**
     * Download the profile picture of the desired user and return the location of where it's stored
     * for reference.
     * @param id - ID of the desired user
     * @return - Uri which points to the image location
     */
    public Uri getProfilePicture(UUID id) {
        final Uri[] results = {null};

        // Get the storage reference of the user it's profile picture
        // Which is stored as "users/<ID>/profile.jpg"
        StorageReference storageRef = ref.child("users/" + id.toString() + "/profile.jpg");

        // Attempt to download the image and get the location Uri
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // If the image was found, set the result to the found Uri
                results[0] = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // If failed, either something went wrong or
                // the user does not have a profile picture
                // Try to load the default profile picture instead;
                results[0] = getDefaultProfilePicture();
            }
        });

        return results[0];
    }

    /**
     * Download the default profile picture and return the location of where it's stored
     * for reference.
     * @return - Uri which points to the image location
     */
    public Uri getDefaultProfilePicture() {
        final Uri[] results = {null};

        // Get the storage reference of the default profile picture
        // Which is stored as "default/profile.jpg"
        StorageReference storageRef = ref.child("default/profile.jpg");

        // Attempt to download the image and get the location Uri
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // If the image was found, set the result to the found Uri
                results[0] = uri;
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

        return results[0];
    }

    /**
     * Upload a profile picture to the firebase storage for a user
     * @param id - The ID of the desired user
     * @param path - The Uri pointer to the picture to be uploaded
     */
    public void setProfilePicture(@NonNull UUID id, @NonNull Uri path){
        StorageReference storageRef = ref.child("users/" + id.toString() +"/profile.jpg");

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

    /**
     * ProfileManager uses the singleton design pattern to make sure that only one ProfileManager
     * exists and can upload/download from the Firebase server at a time.
     * @return - The singleton instance of {@link ProfileManager}
     */
    public static ProfileManager getInstance(){
        if(instance == null){
            instance = new ProfileManager();
        }
        return instance;
    }
}
