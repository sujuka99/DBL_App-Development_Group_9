package com.example.bq.ui.myProfile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.R;
import com.example.bq.datamanager.datatypes.UserData;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel viewModel;

    private TextView fullName;
    private TextView university;
    private TextView biography;
    private TextView study;
    private ProgressBar progressBar;

    private ImageView profilePicture;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // First we initialize the ViewModel of this fragment
        viewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);

        // Then we load in our layout
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);

        // And load the references to our components
        initializeComponents(root);

        // Next up we get the ID of the current logged in user
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //loadProfile(id);
        // And let the ViewModel load the user its data
        loadDataInVM(id);

        return root;
    }

    /**
     * Update all components with the data specified in the {@link UserData} object
     *
     * @param data - The {@link UserData} that will be used to update the profile
     */
    public void updateProfile(UserData data) {
        fullName.setText(data.fullName == null ? "No Name" : data.fullName);
        university.setText(data.university == null ? "No University" : data.university);
        study.setText(data.study == null ? "No Study" : data.study);
        biography.setText(data.biography == null ? "No Biography" : data.biography);
    }

    /**
     * Load a reference to the components in the {@link View}
     *
     * @param root - The {@link View} in which the components can be found
     */
    public void initializeComponents(View root) {
        fullName = root.findViewById(R.id.fullName);
        university = root.findViewById(R.id.university);
        study = root.findViewById(R.id.study);
        biography = root.findViewById(R.id.biography);
        profilePicture = root.findViewById(R.id.profilePicture);
        progressBar = root.findViewById(R.id.profileProgress);
    }

    /**
     * Let the ViewModel load the data of the user and register to data changes
     *
     * @param id
     */
    public void loadDataInVM(String id) {
        viewModel.loadUser(id);

        final Observer<UserData> userDataObserver = new Observer<UserData>() {
            @Override
            public void onChanged(final UserData data) {
                progressBar.setVisibility(View.GONE);
                updateProfile(data);
            }
        };

        final Observer<Bitmap> profilePictureObserver = new Observer<Bitmap>() {
            @Override
            public void onChanged(final Bitmap bmp) {
                profilePicture.setImageBitmap(bmp);
            }
        };

        viewModel.getUserData().observe(this, userDataObserver);
        viewModel.getProfilePicture().observe(this, profilePictureObserver);
    }
}