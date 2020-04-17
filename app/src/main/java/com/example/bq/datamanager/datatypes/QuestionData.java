package com.example.bq.datamanager.datatypes;

import java.util.Collections;
import java.util.HashMap;

/**
 * Record class to store all data regarding a (un)registered Question
 */
public class QuestionData {
    public String title;
    public String description;
    public String author;
    public String id;
    public String study;
    public String timeStamp;

    /**
     * Create a new empty QuestionData object
     */
    public QuestionData() {
    }

    /**
     * Convert a HashMap into a new QuestionData Object
     *
     * @param data A HashMap from which a QuestionData object can be constructed
     */
    public QuestionData(HashMap<String, String> data) {
        this.title = data.containsKey("title") ? data.get("title") : null;
        this.author = data.containsKey("author") ? data.get("author") : null;
        this.study = data.containsKey("study") ? data.get("study") : null;
        this.id = data.containsKey("id") ? data.get("id") : null;
        this.description = data.containsKey("description") ? data.get("description") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
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
        result.put("title", title);
        result.put("study", study);
        result.put("author", author);
        result.put("description", description);
        result.put("timeStamp", timeStamp);

        // Now remove all keys that refer to null, as our fields could be null but we do not
        // wish to retain keys that map to null.
        result.values().removeAll(Collections.singleton(null));
        return result;
    }
}
