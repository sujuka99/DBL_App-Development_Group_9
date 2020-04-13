package com.example.bq.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.datatypes.UserData;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagesViewModel extends ViewModel {

    private MutableLiveData<List<UserData>> userData;

    public MessagesViewModel() {
        userData = new MutableLiveData<>();
        userData.setValue(new ArrayList<UserData>());
    }

    void loadUsers() {
        DataManager.getInstance().getUsers(new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback is created by the loadUsers function
                if (callback.get("action") == FirebaseFunction.FUNCTION_GET_USERLIST) {
                    // If so, load the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // Check if the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, the result is a List of HashMaps
                        List<HashMap<String, Object>> result = (List<HashMap<String, Object>>) response.get("result");
                        // Which we convert into a List of UserData
                        List<UserData> data = new ArrayList<>();
                        for (HashMap<String, Object> user : result) {
                            data.add(new UserData(user));
                        }
                        // And set our responses to this List
                        userData.setValue(data);
                    }
                }
            }
        });
    }

    LiveData<List<UserData>> getUserData() {
        return userData;
    }
}