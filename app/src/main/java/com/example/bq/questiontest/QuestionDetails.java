package com.example.bq.questiontest;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.booktest.BookDetailsFragment;
import com.example.bq.booktest.BookViewModel;
import com.example.bq.datatypes.BookData;
import com.example.bq.datatypes.QuestionData;
import com.example.bq.profiletest.FirebaseObserver;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class QuestionDetails extends Fragment implements FirebaseObserver {

    private QuestionData questionData;

    private QuestionViewModel viewModel;

    private Button deleteQuestion;

    public static QuestionDetails newInstance(QuestionData data) {
        Bundle args = new Bundle();
        args.putSerializable("data", data.toMap());
        QuestionDetails f = new QuestionDetails();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questiondetails, container, false);

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);

        try {
            questionData = new QuestionData((HashMap<String, String>) getArguments().getSerializable("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents(root);
        return root;
    }

    public void initComponents(View root) {

        TextView title = root.findViewById(R.id.questionDetailTitle);
        TextView author = root.findViewById(R.id.questionDetailAuthor);
        TextView description = root.findViewById(R.id.questionDetailBody);
        TextView time = root.findViewById(R.id.questionDetailTimestamp);

        deleteQuestion = root.findViewById(R.id.questionDetailButton);

        title.setText(questionData.title == null ? "No Title" : questionData.title);
        author.setText(questionData.author == null ? "No Author" : questionData.author.split("-")[0]);
        description.setText(questionData.description == null ? "No description" : questionData.description);
        //time.setText(questionData. == null ? "No price" : bookData.price);

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the user its profile
            }
        });


        if (questionData.author.split("-")[1].trim().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid().trim())) {
            initDeleteButton(false);
        } else if (MainActivity.isAdmin) {
            initDeleteButton(true);
        }
    }

    private void initDeleteButton(boolean admin) {
        deleteQuestion.setVisibility(View.VISIBLE);
        deleteQuestion.setText(admin == true ? "Delete" : "Unregister");
        final QuestionDetails fragment = this;
        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteQuestion(questionData, fragment);
            }
        });
    }

    @Override
    public void notifyOfCallback(Object obj) {
        if (obj instanceof HashMap) {
            HashMap<String, Object> result = (HashMap<String, Object>) obj;
            if (result.get("action") == "removeQuestion") {
                if ((boolean) result.get("result")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully removed the book!", Toast.LENGTH_SHORT).show();
                    HomeFragment parent = (HomeFragment) getParentFragment();
                    parent.getChildFragmentManager().popBackStackImmediate();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Could not remove the book!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
