package com.example.bq.datamanager.datatypes;

import java.util.HashMap;

public class QuestionData {
    public String title;
    public String description;
    public String author;
    public String id;
    public String study;
    public String timeStamp;

    public QuestionData() {
    }

    ;

    public QuestionData(HashMap<String, String> data) {
        this.title = data.containsKey("title") ? data.get("title") : null;
        this.author = data.containsKey("author") ? data.get("author") : null;
        this.study = data.containsKey("study") ? data.get("study") : null;
        this.id = data.containsKey("id") ? data.get("id") : null;
        this.description = data.containsKey("description") ? data.get("description") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        if (id != null) {
            result.put("id", id);
        }
        if (title != null) {
            result.put("title", title);
        }
        if (study != null) {
            result.put("study", study);
        }
        if (author != null) {
            result.put("author", author);
        }
        if (description != null) {
            result.put("description", description);
        }
        if (timeStamp != null) {
            result.put("timeStamp", timeStamp);
        }
        return result;
    }
}
