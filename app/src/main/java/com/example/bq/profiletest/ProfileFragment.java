package com.example.bq.profiletest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.R;
import com.example.bq.booktest.BookDetailsFragment;
import com.example.bq.datatypes.BookData;
import com.example.bq.datatypes.UserData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    private String id;

    private TextView fullName;
    private TextView university;
    private TextView biography;
    private TextView study;
    private ProgressBar progressBar;

    private ImageView profilePicture;

    public static ProfileFragment newInstance(String id){
        Bundle args = new Bundle();
        args.putSerializable("id", id);
        ProfileFragment f = new ProfileFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // First we initialize the ViewModel of this fragment
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        try {
            id = (String) getArguments().getSerializable("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then we load in our layout
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // And load the references to our components
        initializeComponents(root);

        // Also load the messageButton
        initializeMessageButton(root);

        // And let the ViewModel load the user its data
        loadDataInVM(id);

        return root;
    }

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

    public void initializeMessageButton(View root) {
        Button messageButton = root.findViewById(R.id.messageButton);

        // We can only start messages with other users, so when the user is viewing their own
        // profile, they cannot select to message themselves
        if (this.id == FirebaseAuth.getInstance().getCurrentUser().getUid()) {
            messageButton.setVisibility(View.GONE);
        } else {
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to message the user
                    Log.d("Profile", "Going to message the user " + id);
                }
            });
        }
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
                profilePicture.setImageBitmap(Bitmap.createScaledBitmap(bmp, profilePicture.getWidth(),
                        profilePicture.getHeight(), false));
            }
        };

        viewModel.getUserData().observe(this, userDataObserver);
        viewModel.getProfilePicture().observe(this, profilePictureObserver);
    }
}
