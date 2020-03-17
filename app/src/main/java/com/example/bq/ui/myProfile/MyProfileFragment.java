package com.example.bq.ui.myProfile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.R;
import com.example.bq.profiletest.DataChangeObserver;
import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.UserData;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileFragment extends Fragment implements DataChangeObserver {

    private MyProfileViewModel viewModel;

    private TextView fullName;
    private TextView university;
    private TextView biography;
    private TextView study;
    private ProgressBar progressBar;

    private ImageView profilePicture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // First we initialize the viewmodel of this fragment
        viewModel =
                ViewModelProviders.of(this).get(MyProfileViewModel.class);

        // Then we load in our layout
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        initializeComponents(root);
        // Next up we get the ID of the current logged in user
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // And request the data we need for the profile
        DataManager.getInstance().loadUserData(id, this);
        // Finally we request the profile picture
        DataManager.getInstance().loadProfilePicture(id, this);
        return root;
    }

    public void updateProfile(UserData data){
        fullName.setText(data.username == null ? "Err: No Name" : data.username);
        university.setText(data.university == null ? "Err: No Uni" : data.university);
        study.setText(data.study == null ? "Err: No Study" : data.study);
        biography.setText(data.biography == null ? "Err: No Bio" : data.biography);
        //profilePicture.setImageURI(data.profilePicture == null ? Uri.EMPTY : data.profilePicture);
    }

    public void initializeComponents(View root){
        fullName = root.findViewById(R.id.fullName);
        university = root.findViewById(R.id.university);
        study = root.findViewById(R.id.study);
        biography = root.findViewById(R.id.biography);
        profilePicture = root.findViewById(R.id.profilePicture);
        progressBar = root.findViewById(R.id.profileProgress);
    }

    @Override
    public void notifyOfDataChange(Object obj) {
        if(obj instanceof UserData){
            progressBar.setVisibility(View.GONE);
            updateProfile((UserData) obj);
        }else if(obj instanceof Uri){
            profilePicture.setImageURI((Uri) obj);
        }
    }
}