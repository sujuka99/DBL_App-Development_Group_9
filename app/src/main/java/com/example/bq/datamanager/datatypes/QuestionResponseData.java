package com.example.bq.datamanager.datatypes;

import java.util.Collections;
import java.util.HashMap;

/**
 * Record class to store data regarding a response to a question
 */
public class QuestionResponseData {
    public String id;
    public String questionID;
    public String author;
    public String body;
    public String timeStamp;

    /**
     * Create a new empty QuestionResponseData object
     */
    public QuestionResponseData() {
    }

    /**
     * Convert a HashMap into a new QuestionResponseData Object
     *
     * @param data A HashMap from which a QuestionResponseData object can be constructed
     */
    public QuestionResponseData(HashMap<String, String> data) {
        this.id = data.containsKey("id") ? data.get("id") : null;
        this.author = data.containsKey("author") ? data.get("author") : null;
        this.questionID = data.containsKey("questionID") ? data.get("questionID") : null;
        this.body = data.containsKey("body") ? data.get("body") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
    }

    /**
     * Convert a QuestionResponseData object to a HashMap for serialization purposes
     *
     * @return A HashMap containing all Non Null data stored in the QuestionResponseData object
     */
    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        // Add all our fields to the map
        result.put("id", id);
        result.put("questionID", questionID);
        result.put("body", body);
        result.put("author", author);
        result.put("timeStamp", timeStamp);

        // Now remove all keys that refer to null, as our fields could be null but we do not
        // wish to retain keys that map to null.
        result.values().removeAll(Collections.singleton(null));
        return result;
    }
}
