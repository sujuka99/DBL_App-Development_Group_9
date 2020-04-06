package com.example.bq.questiontest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.booktest.TimeStamp;
import com.example.bq.datamanager.FirebaseObserver;
import com.example.bq.datamanager.datatypes.QuestionData;
import com.example.bq.datamanager.datatypes.QuestionResponseData;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionDetailsFragment extends Fragment implements FirebaseObserver {

    private QuestionViewModel viewModel;
    private HomeFragment parent;

    private QuestionData questionData;

    private List<QuestionResponseData> responseData = new ArrayList<>();

    private ResponseAdapter adapter;

    private Button deleteQuestion;
    private EditText message;

    public static QuestionDetailsFragment newInstance(QuestionData data) {
        Bundle args = new Bundle();
        args.putSerializable("data", data.toMap());
        QuestionDetailsFragment f = new QuestionDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questiondetails, container, false);

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        parent = (HomeFragment) getParentFragment();

        try {
            questionData = new QuestionData((HashMap<String, String>) getArguments().getSerializable("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadDataInVM();

        initComponents(root);
        initResponse(root);

        initRecycleView(root);
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
        time.setText(TimeStamp.toTime(questionData.timeStamp));

        if (questionData.author.split("-")[1].trim().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid().trim())) {
            initDeleteButton(false);
        } else if (MainActivity.isAdmin) {
            initDeleteButton(true);
        }
    }

    public void initResponse(View root) {
        ImageButton respond = root.findViewById(R.id.btn_send);
        message = root.findViewById(R.id.text_send);
        final EditText message = root.findViewById(R.id.text_send);
        final QuestionDetailsFragment fragment = this;
        respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText() == null || message.getText().toString().trim().length() == 0) {
                    message.setError("You must fill in a response!");
                    return;
                }

                QuestionResponseData data = new QuestionResponseData();
                data.questionID = questionData.id;
                data.timeStamp = Long.toString(Calendar.getInstance().getTimeInMillis());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                data.author = user.getDisplayName() + "-" + user.getUid();
                data.body = message.getText().toString();
                data.id = UUID.randomUUID().toString().replaceAll("-", "");

                viewModel.respondToQuestion(data, fragment);
            }
        });
    }

    private void initRecycleView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.question_responses);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ResponseAdapter(responseData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initDeleteButton(boolean admin) {
        deleteQuestion.setVisibility(View.VISIBLE);
        deleteQuestion.setText(admin == true ? "Delete" : "Unregister");
        final QuestionDetailsFragment fragment = this;
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
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully removed the question!", Toast.LENGTH_SHORT).show();
                    HomeFragment parent = (HomeFragment) getParentFragment();
                    parent.getChildFragmentManager().popBackStackImmediate();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Could not remove the question!", Toast.LENGTH_SHORT).show();
            } else if (result.get("action") == "respondToQuestion") {
                if ((boolean) result.get("result")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully responded!", Toast.LENGTH_SHORT).show();
                    message.setText("");
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Could not respond!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadDataInVM() {
        viewModel.loadResponsesIntoVM(questionData.id);

        final Observer<List<QuestionResponseData>> questionResponseObserver = new Observer<List<QuestionResponseData>>() {
            @Override
            public void onChanged(final List<QuestionResponseData> data) {
                responseData.clear();
                responseData.addAll(data);
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getResponses().observe(this, questionResponseObserver);
    }
}
