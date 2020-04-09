package com.example.bq.datamanager.datatypes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookData {
    public String title;
    public String author;
    public String price;
    public String location;
    public String description;
    public String seller;
    public String id;
    public String study;
    public String timeStamp;

    public BookData(HashMap<String, String> data) {
        this.title = data.containsKey("title") ? data.get("title") : null;
        this.author = data.containsKey("author") ? data.get("author") : null;
        this.study = data.containsKey("study") ? data.get("study") : null;
        this.id = data.containsKey("id") ? data.get("id") : null;
        this.location = data.containsKey("location") ? data.get("location") : null;
        this.description = data.containsKey("description") ? data.get("description") : null;
        this.price = data.containsKey("price") ? data.get("price") : null;
        this.seller = data.containsKey("seller") ? data.get("seller") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
    }

    public BookData() {
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();

        // Add all our fields to the map
        result.put("id", id);
        result.put("title", title);
        result.put("study", study);
        result.put("author", author);
        result.put("price", price);
        result.put("location", location);
        result.put("description", description);
        result.put("seller", seller);
        result.put("timeStamp", timeStamp);

        // Now remove all keys that refer to null, as our fields could be null but we do not
        // wish to retain keys that map to null.
        // This allows for easier unit testing, as we now only have to test that
        // For all keys, getValue != null;
        result.values().removeAll(Collections.singleton(null));
        return result;
    }
}
