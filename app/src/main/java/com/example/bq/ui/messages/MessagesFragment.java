package com.example.bq.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bq.MessageActivity;
import com.example.bq.R;
import com.example.bq.datatypes.QuestionData;
import com.example.bq.datatypes.UserData;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesFragment extends Fragment {

    //private static final String TAG = "MessagesFragment";

    private MessagesViewModel viewModel;
    private HomeFragment parent;
    private MessagesAdapter adapter;

    private List<UserData> userData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        final TextView textView = root.findViewById(R.id.text_messages);
        textView.setText("My Messages");

        viewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);

        loadDataInVM();
        initRecycleView(root);

        return root;
    }

    private void loadDataInVM(){
        viewModel.loadUsers();

        final Observer<List<UserData>> userDataObserver = new Observer<List<UserData>>() {
            @Override
            public void onChanged(final List<UserData> data) {
                userData.clear();
                userData.addAll(data);
                Log.d("DataTest", userData.size() + "");
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getUserData().observe(this, userDataObserver);
    }

    private void initRecycleView(View root){
        RecyclerView recyclerView = root.findViewById(R.id.messages_recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new MessagesAdapter(userData, getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}