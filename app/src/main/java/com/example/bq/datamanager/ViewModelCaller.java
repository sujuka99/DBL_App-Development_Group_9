package com.example.bq.datamanager;

/**
 * An interface to handle asynchronous callbacks between ViewModels and fragments/activities after
 * the ViewModel has received a callback from the firebase server
 */
public interface ViewModelCaller {

    /**
     * Give a callback to the caller
     *
     * @param obj Can be anything that the caller might need to process/verify
     */
    void callback(Object obj);
}
