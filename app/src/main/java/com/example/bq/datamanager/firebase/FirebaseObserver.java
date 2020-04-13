package com.example.bq.datamanager.firebase;

import java.util.HashMap;

/**
 * Interface to implement the Observer pattern to handle Firebase Asynchronous callbacks when
 * downloading/uploading data from/to the Firebase server
 */
public interface FirebaseObserver {

    /**
     * Listener method to be called when a Firebase callback is handled
     *
     * @param obj - The object returned by the callback
     */
    void notifyOfCallback(HashMap<String, Object> obj);
}
