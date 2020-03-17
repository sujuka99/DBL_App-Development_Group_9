package com.example.bq.profiletest;

public class UserData {
    public String username;
    public String biography;
    public String study;
    public String university;
    public String id;

    public UserData(UserData data){
        this.username = data.username;
        this.biography = data.biography;
        this.study = data.study;
        this.university = data.university;
        this.id = data.id;
    }

    public UserData(){}

    public String toString(){
        return "{username: " + (username == null ? "null":username) + ", id: "+ (id == null ? "null":id) +
        ", study: " + (study == null ? "null":study) +
        ", biography: " + (biography == null ? "null":biography) +
                ", university: " + (university == null ? "null":university) + "}";
    }
}
