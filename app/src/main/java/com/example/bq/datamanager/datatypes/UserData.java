package com.example.bq.datamanager.datatypes;

import java.util.Collections;
import java.util.HashMap;

/**
 * Record class to store all data regarding Users
 */
public class UserData {

    public String fullName;
    public String biography;
    public String study;
    public String university;
    public String id;

    /**
     * Create a new empty UserData object
     */
    public UserData() {
    }

    /**
     * Convert a HashMap into a new UserData Object
     *
     * @param data A HashMap from which a UserData object can be constructed
     */
    public UserData(HashMap<String, Object> data) {
        this.fullName = data.containsKey("fullName") ? (String) data.get("fullName") : null;
        this.biography = data.containsKey("biography") ? (String) data.get("biography") : null;
        this.study = data.containsKey("study") ? (String) data.get("study") : null;
        this.id = data.containsKey("id") ? (String) data.get("id") : null;
        this.university = data.containsKey("university") ? (String) data.get("university") : null;
    }

    /**
     * Convert a UserData object to a HashMap for serialization purposes
     *
     * @return A HashMap containing all Non Null data stored in the UserData object
     */
    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        // Add all our fields to the map
        result.put("id", id);
        result.put("fullName", fullName);
        result.put("study", study);
        result.put("biography", biography);
        result.put("university", university);

        // Now remove all keys that refer to null, as our fields could be null but we do not
        // wish to retain keys that map to null.
        result.values().removeAll(Collections.singleton(null));
        return result;
    }
}

