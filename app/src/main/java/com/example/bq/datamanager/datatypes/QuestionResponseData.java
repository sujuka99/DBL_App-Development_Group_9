package com.example.bq.datamanager.datatypes;

import java.util.HashMap;

public class QuestionResponseData {
    public String id;
    public String questionID;
    public String author;
    public String body;
    public String timeStamp;

    public QuestionResponseData() {
    }

    public QuestionResponseData(HashMap<String, String> data) {
        this.id = data.containsKey("id") ? data.get("id") : null;
        this.author = data.containsKey("author") ? data.get("author") : null;
        this.questionID = data.containsKey("questionID") ? data.get("questionID") : null;
        this.body = data.containsKey("body") ? data.get("body") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        if (id != null) {
            result.put("id", id);
        }
        if (questionID != null) {
            result.put("questionID", questionID);
        }
        if (author != null) {
            result.put("author", author);
        }
        if (body != null) {
            result.put("body", body);
        }
        if (author != null) {
            result.put("author", author);
        }
        return result;
    }
}
