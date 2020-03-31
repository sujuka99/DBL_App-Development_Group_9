package com.example.bq.datatypes;

import android.location.Location;

import java.util.HashMap;

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
        this.title = data.containsKey("title") ?  data.get("title") : null;
        this.author = data.containsKey("author") ?  data.get("author") : null;
        this.study = data.containsKey("study") ?  data.get("study") : null;
        this.id = data.containsKey("id") ?  data.get("id") : null;
        this.location = data.containsKey("location") ?  data.get("location") : null;
        this.description = data.containsKey("description") ?  data.get("description") : null;
        this.price = data.containsKey("price") ?  data.get("price") : null;
        this.seller = data.containsKey("seller") ?  data.get("seller") : null;
        this.timeStamp = data.containsKey("timeStamp") ? data.get("timeStamp") : null;
    }

    public BookData() {
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
        if (price != null) {
            result.put("price", price);
        }
        if (location != null) {
            result.put("location", location);
        }
        if (description != null) {
            result.put("description", description);
        }
        if (seller != null) {
            result.put("seller", seller);
        }
        if(timeStamp != null){
            result.put("timeStamp", timeStamp);
        }
        return result;
    }
}
