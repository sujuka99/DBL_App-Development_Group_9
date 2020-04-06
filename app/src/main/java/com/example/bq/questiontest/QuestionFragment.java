package com.example.bq.questiontest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.R;
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

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);

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

    private void loadDataInVM() {
        viewModel.loadQuestionsIntoVM(parent.major);

        final Observer<List<QuestionData>> questionDataObserver = new Observer<List<QuestionData>>() {
            @Override
            public void onChanged(final List<QuestionData> data) {
                questionData.clear();
                questionData.addAll(data);
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getQuestions().observe(this, questionDataObserver);
    }

    public void initRecycleView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recycle_questions);
        recyclerView.setHasFixedSize(true); //----> Different from books
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()); // ----> Different from books
        adapter = new QuestionAdapter(questionData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void loadQuestionDetails(int position) {
        parent.loadQuestionDetails(questionData.get(position));
    }
}
