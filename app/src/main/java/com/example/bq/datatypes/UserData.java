package com.example.bq.datatypes;

import java.util.HashMap;

public class UserData {// RECORD TYPE

    public String fullName;
    public String biography;
    public String study;
    public String university;
    public String id;

    public UserData() {
    }

    public UserData(HashMap<String, Object> data) {
        this.fullName = data.containsKey("fullName") ? (String) data.get("fullName") : null;
        this.biography = data.containsKey("biography") ? (String) data.get("biography") : null;
        this.study = data.containsKey("study") ? (String) data.get("study") : null;
        this.id = data.containsKey("id") ? (String) data.get("id") : null;
        this.university = data.containsKey("university") ? (String) data.get("university") : null;
    }

    public String toString() {
        return "{username: " + (fullName == null ? "null" : fullName) +
                ", id: " + (id == null ? "null" : id) +
                ", study: " + (study == null ? "null" : study) +
                ", biography: " + (biography == null ? "null" : biography) +
                ", university: " + (university == null ? "null" : university) + "}";
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        if (id != null) {
            result.put("id", id);
        }
        if (fullName != null) {
            result.put("fullName", fullName);
        }
        if (study != null) {
            result.put("study", study);
        }
        if (university != null) {
            result.put("university", university);
        }
        if (biography != null) {
            result.put("biography", biography);
        }
        return result;
    }
}

