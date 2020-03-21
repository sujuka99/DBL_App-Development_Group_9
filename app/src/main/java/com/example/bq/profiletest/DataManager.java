package com.example.bq.profiletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.bq.R;
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

public class DataManager {

    public static DataManager instance;

    private DataManager() {
    }

    public void loadUserData(final String id, final FirebaseObserver observer) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(id);

        // We get notified once the data is loaded the first time
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d("Database:", "DataSnapshot not found!");
                }

                UserData data = dataSnapshot.getValue(UserData.class);

                observer.notifyOfCallback(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("setProfilePicture", "Failed due to an: " +
                        databaseError.getMessage());
            }
        });
    }

    public void updateUserData(@NonNull String id, @NonNull UserData data) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(id);

        userRef.setValue(data);
    }

    /**
     * Attempt toownload the profile picture data of the desired user and send it to the observer
     *
     * @param id - ID of the desired user
     * @param observer - The {@link FirebaseObserver} object that will handle the callback
     */
    public void loadProfilePicture(final String id, final FirebaseObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the user it's profile picture
        // Which is stored as "users/<ID>/profile.jpg"
        StorageReference storageRef = ref.child("users/" + id + "/profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        // Attempt to download the image and get the bytes
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // If the image was found, convert the result into a Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Send the Bitmap to the observer
                observer.notifyOfCallback(bmp);
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
     * Download the default profile picture and send the data to the observer
     *
     * @param observer - The {@link FirebaseObserver} object that will handle the callback
     */
    private void loadDefaultPicture(final FirebaseObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the default profile picture
        // Which is stored as "default/profile.jpg"
        StorageReference storageRef = ref.child("default/profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        // Attempt to download the image and get the bytes
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // If the image was found, convert the result into a Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Send the Bitmap to the observer
                observer.notifyOfCallback(bmp);
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
     *
     * @param id   - The ID of the desired user
     * @param path - The Uri pointer to the picture to be uploaded
     */
    public void setProfilePicture(@NonNull String id, @NonNull Uri path) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference storageRef = ref.child("users/" + id + "/profile.jpg");

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

    public static DataManager getInstance() {
        return instance == null ? (instance = new DataManager()) : instance;
    }
}
