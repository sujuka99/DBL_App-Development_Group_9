package com.example.bq.profiletest;

import java.util.HashMap;

public class UserData {// RECORD TYPE
    public String fullName;
    public String biography;
    public String study;
    public String university;
    public String id;
    private UserRole role;

    public UserData(UserData data) {
        this.fullName = data.fullName;
        this.biography = data.biography;
        this.study = data.study;
        this.university = data.university;
        this.id = data.id;
        this.role = data.role;
    }

    public UserData (HashMap<String, String> data){
        this.fullName = data.getOrDefault("fullName", "");
        this.biography = data.getOrDefault("biography", "");
        this.study = data.getOrDefault("study", "");
        this.id = data.getOrDefault("id", "");
        this.university = data.getOrDefault("university", "");
        this.role = UserRole.valueOf(data.getOrDefault("role", "USER"));
    }

    public UserData() {
        role = UserRole.USER;
    }

    public String toString() {
        return "{username: " + (fullName == null ? "null" : fullName) + ", id: " + (id == null ? "null" : id) +
                ", study: " + (study == null ? "null" : study) +
                ", biography: " + (biography == null ? "null" : biography) +
                ", university: " + (university == null ? "null" : university) + "}";
    }

    public HashMap<String, String> toMap(){
        HashMap<String, String> result  = new HashMap<>();
            result.put("id", id == null ? "" : id);
            result.put("fullName", fullName == null ? "" : fullName);
            result.put("study", study == null ? "" : study);
            result.put("biography" , biography == null ? "" : biography);
            result.put("role", role == null ? "" : role.name());
        return result;
    }
}

