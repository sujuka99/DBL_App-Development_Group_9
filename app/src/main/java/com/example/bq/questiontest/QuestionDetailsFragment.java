package com.example.bq.questiontest;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.booktest.TimeStamp;
import com.example.bq.datamanager.ViewModelCaller;
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

public class QuestionDetailsFragment extends Fragment {

    private QuestionViewModel viewModel;
    private HomeFragment parent;

    private QuestionData questionData;

    private List<QuestionResponseData> responseData = new ArrayList<>();

    private ResponseAdapter adapter;

    private Button deleteQuestion;

    /**
     * Create a new QuestionDetailsFragment with a reference to the desired QuestionData
     *
     * @param data The QuestionData object the Fragment will display
     * @return A new instance of QuestionDetailsFragment with data as extra data
     */
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

        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
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

    /**
     * Initialize all detail components in the layout
     *
     * @param root Root view in which the components can be found
     */
    private void initComponents(View root) {

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

    /**
     * Initialize all components that have functionality regarding responding to the question
     *
     * @param root Root view in which the components can be found
     */
    private void initResponse(View root) {
        ImageButton respond = root.findViewById(R.id.btn_send);
        final EditText message = root.findViewById(R.id.text_send);
        respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText() == null || TextUtils.isEmpty(message.getText())) {
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

                viewModel.respondToQuestion(data, new ViewModelCaller() {
                    @Override
                    public void callback(Object obj) {
                        if (obj instanceof String) {
                            Toast.makeText(getActivity().getApplicationContext(), (String) obj, Toast.LENGTH_SHORT).show();
                        } else if ((boolean) obj) {
                            Toast.makeText(getActivity().getApplicationContext(), "Successfully responded!", Toast.LENGTH_SHORT).show();
                            message.setText("");
                        }
                    }
                });
            }
        });
    }

    /**
     * Initialize the recycle view to display all responses
     *
     * @param root Root view in which the recycler view can be found
     */
    private void initRecycleView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.question_responses);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ResponseAdapter(responseData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Initialize the delete/unregister button
     *
     * @param admin Whether the user has admin privileges for this question
     */
    private void initDeleteButton(boolean admin) {
        deleteQuestion.setVisibility(View.VISIBLE);
        deleteQuestion.setText(admin ? "Delete" : "Unregister");
        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteQuestion(questionData, new ViewModelCaller() {
                    @Override
                    public void callback(Object obj) {
                        if (obj instanceof String) {
                            Toast.makeText(getActivity().getApplicationContext(), (String) obj, Toast.LENGTH_SHORT).show();
                        } else if ((boolean) obj) {
                            Toast.makeText(getActivity().getApplicationContext(), "Successfully removed the question!", Toast.LENGTH_SHORT).show();
                            parent.getChildFragmentManager().popBackStackImmediate();
                        }
                    }
                });
            }
        });
    }

    /**
     * Load all question responses into the view model, which will trigger an observe function to
     * which we will display all responses in the recycler view
     */
    private void loadDataInVM() {
        viewModel.loadResponsesIntoVM(questionData.id, new ViewModelCaller() {
            @Override
            public void callback(Object obj) {
                if (obj instanceof String) {
                    Toast.makeText(getActivity().getApplicationContext(), (String) obj, Toast.LENGTH_SHORT).show();
                } else if ((boolean) obj) {
                    Toast.makeText(getActivity().getApplicationContext(), "Loaded responses", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Observer<List<QuestionResponseData>> questionResponseObserver = new Observer<List<QuestionResponseData>>() {
            @Override
            public void onChanged(final List<QuestionResponseData> data) {
                responseData.clear();
                responseData.addAll(data);
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getResponses().observe(getViewLifecycleOwner(), questionResponseObserver);
    }
}
