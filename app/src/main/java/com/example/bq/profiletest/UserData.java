package com.example.bq.profiletest;

public class UserData {// RECORD TYPE
    public String fullName;
    public String biography;
    public String study;
    public String university;
    public String id;

    public UserData(UserData data) {
        this.fullName = data.fullName;
        this.biography = data.biography;
        this.study = data.study;
        this.university = data.university;
        this.id = data.id;
    }

    public UserData() {
    }

    public String toString() {
        return "{username: " + (fullName == null ? "null" : fullName) + ", id: " + (id == null ? "null" : id) +
                ", study: " + (study == null ? "null" : study) +
                ", biography: " + (biography == null ? "null" : biography) +
                ", university: " + (university == null ? "null" : university) + "}";
    }
}
