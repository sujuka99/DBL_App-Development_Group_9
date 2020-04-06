package com.example.bq.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.FirebaseObserver;
import com.example.bq.datamanager.datatypes.UserData;

import java.util.ArrayList;
import java.util.List;

public class MessagesViewModel extends ViewModel {

    private MutableLiveData<List<UserData>> userData;

    public MessagesViewModel() {
        userData = new MutableLiveData<>();
        userData.setValue(new ArrayList<UserData>());
    }

    public void loadUsers() {
        DataManager.getInstance().getUsers(new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if (obj instanceof List) {
                    userData.setValue((List<UserData>) obj);
                }
            }
        });
    }

    public LiveData<List<UserData>> getUserData() {
        return userData;
    }
}