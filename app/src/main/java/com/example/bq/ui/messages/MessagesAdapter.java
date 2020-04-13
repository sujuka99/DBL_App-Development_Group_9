package com.example.bq.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bq.MessageActivity;
import com.example.bq.R;
import com.example.bq.datamanager.datatypes.UserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private List<UserData> userData;
    private Context messageContext;

    MessagesAdapter(List<UserData> userData, Context messageContext) {
        this.userData = userData;
        this.messageContext = messageContext;
    }

    @NonNull
    @Override
    public MessagesAdapter.MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_messages_listitem, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessagesViewHolder holder, final int position) {

        String defaultLink = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png";
        Glide.with(messageContext)
                .asBitmap()
                .load(defaultLink)
                .into(holder.profilePicture);

        final UserData data = userData.get(position);
        holder.fullName.setText(data.fullName);
        holder.lastMessage.setText("Last Message Unknown");
        holder.messageTime.setText("Message Time Unknown");

        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(messageContext, data.fullName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageContext, MessageActivity.class);
                intent.putExtra("userid", data.id);
                messageContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicture;
        TextView fullName;
        TextView lastMessage;
        TextView messageTime;
        ConstraintLayout messageLayout;

        MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.message_picture);
            fullName = itemView.findViewById(R.id.message_username);
            lastMessage = itemView.findViewById(R.id.last_message);
            messageTime = itemView.findViewById(R.id.message_time);
            messageLayout = itemView.findViewById(R.id.messages_layout2);
        }
    }
}
