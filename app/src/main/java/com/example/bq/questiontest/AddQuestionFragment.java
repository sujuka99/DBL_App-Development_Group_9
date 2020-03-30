package com.example.bq.questiontest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.R;
import com.example.bq.datatypes.QuestionData;
import com.example.bq.profiletest.FirebaseObserver;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class AddQuestionFragment extends Fragment implements FirebaseObserver {

    private EditText questionTitle;
    private EditText questionDescription;

    QuestionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addquestion, container, false);
        viewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);

        initializeComponents(root);

        return root;
    }

    private void initializeComponents(View root) {
        questionTitle = root.findViewById(R.id.addQuestionTitle);
        questionDescription = root.findViewById(R.id.addQuestionBody);

        Button registerQuestion = root.findViewById(R.id.addQuestionButton);
        registerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerQuestion();
            }
        });
    }

    private void registerQuestion() {
        QuestionData data = new QuestionData();

        if (questionTitle.getText() == null || questionTitle.getText().toString().trim().length() == 0) {
            questionTitle.setError("A question must have a title!");
            return;
        }
        if (questionDescription.getText() == null || questionDescription.getText().toString().trim().length() == 0) {
            questionDescription.setError("A question must have a description!");
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getActivity().getApplicationContext(), "You must be logged in to do that!", Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent());
            getActivity().finish();
        }

        HomeFragment parent = (HomeFragment) getParentFragment();

        data.title = questionTitle.getText().toString();
        data.author = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        data.study = parent.major;
        data.id = UUID.randomUUID().toString().replaceAll("-", "");

        viewModel.addQuestion(data, this);
    }

    @Override
    public void notifyOfCallback(Object obj) {
        if ((boolean) obj) {
            Toast.makeText(getActivity().getApplicationContext(), "Successfully added a question!", Toast.LENGTH_SHORT).show();
            HomeFragment parent = (HomeFragment) getParentFragment();
            parent.getChildFragmentManager().popBackStackImmediate();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Unsuccessful when attempting to add the question!", Toast.LENGTH_SHORT).show();
        }
    }
}
