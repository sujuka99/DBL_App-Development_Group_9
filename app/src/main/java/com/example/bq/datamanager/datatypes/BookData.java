package com.example.bq.datamanager.datatypes;

import java.util.Collections;
import java.util.HashMap;

/**
 * Record class to store and edit all data regarding a (un)registered book
 */
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

    /**
     * Convert a HashMap into a new BookData Object
     *
     * @param data A HashMap from which a BookData object can be constructed
     */
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

    /**
     * Create a new empty BookData object
     */
    public BookData() {
    }

    /**
     * Convert a BookData object to a HashMap for serialization purposes
     *
     * @return A HashMap containing all Non Null data stored in the BookData object
     */
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
        result.values().removeAll(Collections.singleton(null));
        return result;
    }
}
