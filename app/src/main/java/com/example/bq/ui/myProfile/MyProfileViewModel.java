package com.example.bq.ui.myProfile;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.FirebaseObserver;
import com.example.bq.profiletest.UserData;

public class MyProfileViewModel extends ViewModel implements FirebaseObserver {

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
        if (this.id == id) {
            // Just 'reset' the values to trigger the onChanged event
            userData.setValue(userData.getValue());
            profilePicture.setValue(profilePicture.getValue());

            return;
        }

        // Otherwise, we load the profile picture and userdata from the database
        this.id = id;
        DataManager.getInstance().loadProfilePicture(id, this);
        DataManager.getInstance().loadUserData(id, this);
        DataManager.getInstance().getUserFromDatabase(id, this);
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
     * Update the LiveData object of the UserData with a new UserData object
     *
     * @param data - The new data to be set
     */
    public void setUserData(UserData data) {
        userData.setValue(data);
    }

    /**
     * Update the LiveData object of the profile picture with a new Uri object
     *
     * @param uri - The new uri to be set
     */
    public void setProfilePicture(Uri uri) {
        profilePicture.setValue(uri);
    }

    /**
     * Handle the Firebase callback of loading the profile picture and user data from the server
     *
     * @param obj - Either a UserData object or Uri object
     */
    @Override
    public void notifyOfCallback(Object obj) {
        if (obj instanceof UserData) {
            userData.setValue((UserData) obj);
        } else if (obj instanceof Uri) {
            profilePicture.setValue((Uri) obj);
        }
    }
}