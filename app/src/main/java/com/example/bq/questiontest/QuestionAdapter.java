package com.example.bq.questiontest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.R;
import com.example.bq.booktest.TimeStamp;
import com.example.bq.datamanager.datatypes.QuestionData;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<QuestionData> questionData;
    private QuestionFragment fragment;

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView questionTitle;
        TextView questionAuthor;
        TextView questionAge;
        ConstraintLayout questionLayout;


        public QuestionViewHolder(View v) {
            super(v);
            questionTitle = v.findViewById(R.id.question_title);
            questionAuthor = v.findViewById(R.id.question_author);
            questionAge = v.findViewById(R.id.question_age);
            questionLayout = v.findViewById(R.id.question_layout);
        }
    }

    public QuestionAdapter(List<QuestionData> data, QuestionFragment fragment) {
        this.questionData = data;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_questions_listitem, parent, false);
        QuestionViewHolder holder = new QuestionViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, final int position) {
        QuestionData data = questionData.get(position);
        holder.questionTitle.setText(data.title);
        holder.questionAuthor.setText(data.author.split("-")[0]);
        holder.questionAge.setText(TimeStamp.toTime(data.timeStamp));

        holder.questionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.loadQuestionDetails(position);
                Toast.makeText(v.getContext(), "Load question details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionData.size();
    }
}
