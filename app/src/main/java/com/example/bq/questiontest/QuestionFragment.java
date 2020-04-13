package com.example.bq.questiontest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.R;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.QuestionData;
import com.example.bq.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {

    private QuestionAdapter adapter;

    private QuestionViewModel viewModel;
    private HomeFragment parent;

    private List<QuestionData> questionData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_questions, container, false);

        parent = (HomeFragment) getParentFragment();

        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        loadDataInVM();

        initRecycleView(root);

        Button createQuestionBtn = root.findViewById(R.id.button_create_question);
        createQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.addQuestion();
            }
        });
        return root;
    }

    /**
     * Let the viewmodel load all questions, after which we will observe a change in the questions
     * and we will update the recycler view with the new questions
     */
    private void loadDataInVM() {
        viewModel.loadQuestionsIntoVM(parent.major, new ViewModelCaller() {
            @Override
            public void callback(Object obj) {
                if (obj instanceof String) {
                    Toast.makeText(getActivity().getApplicationContext(), (String) obj, Toast.LENGTH_SHORT).show();
                } else if ((boolean) obj) {
                    Toast.makeText(getActivity().getApplicationContext(), "Loaded questions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Observer<List<QuestionData>> questionDataObserver = new Observer<List<QuestionData>>() {
            @Override
            public void onChanged(final List<QuestionData> data) {
                questionData.clear();
                questionData.addAll(data);
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getQuestions().observe(getViewLifecycleOwner(), questionDataObserver);
    }

    /**
     * Initialize the recycler view to handle questions
     * @param root Root view in which the recycler view can be found
     */
    private void initRecycleView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recycle_questions);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new QuestionAdapter(questionData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Open the question details fragment for the question located at the desired position
     * @param position Position in questionData of the question of which the details must be displayed
     */
    void loadQuestionDetails(int position) {
        parent.loadQuestionDetails(questionData.get(position));
    }
}
