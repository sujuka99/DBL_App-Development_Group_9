package com.example.bq.profiletest;

import android.net.Uri;

public class ProfileData {
    public String id;
    public Uri profilePicture;
    public String username;
    public String biography;
    public String study;
    public String university;

    public ProfileData(String id, String username, Uri profilePicture){
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public ProfileData(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
