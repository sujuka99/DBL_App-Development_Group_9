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
import com.example.bq.datatypes.QuestionResponseData;

import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder> {

    private static final String TAG = "ResponseAdapter";

    private List<QuestionResponseData> questionResponseData;
    private QuestionFragment fragment;

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView responseBody;
        TextView responseAuthor;
        TextView responseTimestamp;
        ConstraintLayout questionLayout;


        public ResponseViewHolder(View v) {
            super(v);
            responseBody = v.findViewById(R.id.responseText);
            responseAuthor = v.findViewById(R.id.responseAuthor);
            responseTimestamp = v.findViewById(R.id.responseTimestamp);
            questionLayout = v.findViewById(R.id.response_layout);
        }
    }

    public ResponseAdapter(List<QuestionResponseData> data, QuestionFragment fragment) {
        this.questionResponseData = data;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_questiondetails_listitem, parent, false);
        ResponseAdapter.ResponseViewHolder holder = new ResponseAdapter.ResponseViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseAdapter.ResponseViewHolder holder, final int position) {

        QuestionResponseData data = questionResponseData.get(position);
        holder.responseBody.setText(data.body);
        holder.responseAuthor.setText("Answered by " + data.author.split("-")[0].trim());
        holder.responseTimestamp.setText("No Time"); //----> Fix time stamps

        holder.questionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.loadQuestionDetails(position);
                Toast.makeText(v.getContext(), "Load response details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionResponseData.size();
    }
}
