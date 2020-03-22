package com.example.bq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Questions extends AppCompatActivity {

    TextView pageTitle;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<List<String>> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        
        createDataset();

        pageTitle = findViewById(R.id.question_page);
        pageTitle.setText(MainActivity.major + " Questions");

        recyclerView = findViewById(R.id.recycle_questions);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        layoutManager =  new LinearLayoutManager(this);
        adapter = new QuestionAdapter(this, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void createDataset() {
        dataSet = new ArrayList<>();
        dataSet.add(new ArrayList<String>());
        dataSet.get(0).add("How can I answer this question?");
        dataSet.get(0).add("CrazyGirl99");
        dataSet.get(0).add("19m");
        dataSet.add(new ArrayList<String>());
        dataSet.get(1).add("How do I solve this error?");
        dataSet.get(1).add("CoderBoy74");
        dataSet.get(1).add("4h");
        dataSet.add(new ArrayList<String>());
        dataSet.get(2).add("Who do I contact for this problem?");
        dataSet.get(2).add("NeedHelp55");
        dataSet.get(2).add("1d");
        dataSet.add(new ArrayList<String>());
        dataSet.get(3).add("Question Title");
        dataSet.get(3).add("<author>");
        dataSet.get(3).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(4).add("Question Title");
        dataSet.get(4).add("<author>");
        dataSet.get(4).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(5).add("Question Title");
        dataSet.get(5).add("<author>");
        dataSet.get(5).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(6).add("Question Title");
        dataSet.get(6).add("<author>");
        dataSet.get(6).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(7).add("Question Title");
        dataSet.get(7).add("<author>");
        dataSet.get(7).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(8).add("Question Title");
        dataSet.get(8).add("<author>");
        dataSet.get(8).add("<time>");
        dataSet.add(new ArrayList<String>());
        dataSet.get(9).add("Question Title");
        dataSet.get(9).add("<author>");
        dataSet.get(9).add("<time>");
    }
}

class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private static final String TAG = "QuestionAdapter";

    private List<List<String>> qDataSet;
    private Context qContext;

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

    public QuestionAdapter(Context qContext, List<List<String>> qDataSet) {
        this.qDataSet = qDataSet;
        this.qContext = qContext;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_questions_listitem, parent, false);
        QuestionViewHolder holder = new QuestionViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, final int position) {

        holder.questionTitle.setText(qDataSet.get(position).get(0));
        holder.questionAuthor.setText("Asked by " + qDataSet.get(position).get(1));
        holder.questionAge.setText(qDataSet.get(position).get(2) + " ago");

        holder.questionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(qContext, qDataSet.get(position).get(0), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return qDataSet.size();
    }
}
