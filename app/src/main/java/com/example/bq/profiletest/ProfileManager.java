package com.example.bq.profiletest;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
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
     * Set the {@link ProfileData} for the
     * @param id
     * @param data
     */
    public void setProfileData(UUID id, ProfileData data){

    }

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
}
