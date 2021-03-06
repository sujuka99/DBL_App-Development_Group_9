package com.example.bq.ui.myProfile;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.datatypes.UserData;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;

import java.util.HashMap;

public class MyProfileViewModel extends ViewModel {

    private String id;

    private MutableLiveData<UserData> userData;
    private MutableLiveData<Uri> profilePicture;

    public MyProfileViewModel() {
        id = "";
        userData = new MutableLiveData<>();
        profilePicture = new MutableLiveData<>();
    }

    /**
     * Let the ViewModel attempt to load the data of the desired user.
     * If the data is already loaded, the ViewModel will trigger an onChanged event to subscribed
     * observers.
     *
     * @param id - ID of the desired user
     */
    public void loadUser(String id) {
        // If we already have loaded the data of this user from the database
        if (this.id.equals(id)) {
            // Just 'reset' the values to trigger the onChanged event
            userData.setValue(userData.getValue());
            profilePicture.setValue(profilePicture.getValue());
            return;
        }

        // Otherwise, we load the profile picture and userdata from the database
        this.id = id;
        DataManager.getInstance().downloadImageFromStorage("users/" + id + "/profile.jpg", new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                profilePicture.setValue((Uri) callback.get("uri"));
            }
        });
        DataManager.getInstance().getUserFromDatabase(id, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_GET_USER)) {
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");

                    if ((boolean) response.get("success")) {
                        HashMap<String, Object> result = (HashMap<String, Object>) response.get("result");
                        userData.setValue(new UserData(result));
                    }
                }
            }
        });
    }

    /**
     * Return the LiveData object that contains the current UserData
     */
    public LiveData<UserData> getUserData() {
        return userData;
    }

    /**
     * Return the LiveData object that contains the current Uri
     */
    public LiveData<Uri> getProfilePicture() {
        return profilePicture;
    }

    /**
     * Update the LiveData object of the profile picture with a new Bitmap object
     *
     * @param bmp - The new bitmap to be set
     */
    public void setProfilePicture(Uri bmp) {
        profilePicture.setValue(bmp);
    }
}