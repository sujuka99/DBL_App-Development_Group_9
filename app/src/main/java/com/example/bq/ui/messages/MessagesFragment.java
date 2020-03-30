package com.example.bq.ui.messages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bq.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesFragment extends Fragment {

    //private static final String TAG = "MessagesFragment";

    //private MessagesViewModel messagesViewModel;

    List<String> profilePictures;
    List<String> fullNames;
    List<String> lastMessages;
    List<String> messageTimes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        final TextView textView = root.findViewById(R.id.text_messages);
        textView.setText("My Messages");

        //Log.d(TAG, "MessagesFragment created");

        initData();

        //Log.d(TAG, "data initialised");

        RecyclerView recyclerView = root.findViewById(R.id.messages_recyclerview);
        //Log.d(TAG, "recyclerView found");
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //Log.d(TAG, "divider added");
        MessagesAdapter adapter = new MessagesAdapter(profilePictures, fullNames, lastMessages, messageTimes, getActivity());
        //Log.d(TAG, "MessagesAdapter created");
        recyclerView.setAdapter(adapter);
        //Log.d(TAG, "MessagesFragment set as adapter for recyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //Log.d(TAG, "LinearLayout created");
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Log.d(TAG, "LinearLayout orientation set");
        recyclerView.setLayoutManager(layoutManager);
        //Log.d(TAG, "LinearLayout set as LayoutManager for recyclerView");

        return root;
    }

    private void initData() {
        profilePictures = new ArrayList<>();
        fullNames = new ArrayList<>();
        lastMessages = new ArrayList<>();
        messageTimes = new ArrayList<>();

        profilePictures.add("https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Mark_Zuckerberg_F8_2018_Keynote_%28cropped_2%29.jpg/1200px-Mark_Zuckerberg_F8_2018_Keynote_%28cropped_2%29.jpg");
        fullNames.add("Mark Zuckerberg");
        lastMessages.add("What are you doing tonight?");
        messageTimes.add("1h ago");

        profilePictures.add("https://www.biography.com/.image/t_share/MTY2MzU3Nzk2OTM2MjMwNTkx/elon_musk_royal_society.jpg");
        fullNames.add("Elon Musk");
        lastMessages.add("I have a job opportunity for you.");
        messageTimes.add("2h ago");

        profilePictures.add("https://pmcvariety.files.wordpress.com/2019/03/trump-1.jpg?w=1000&h=563&crop=1");
        fullNames.add("Donald Trump");
        lastMessages.add("Fake news!");
        messageTimes.add("1d ago");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
        fullNames.add("Anonymous");
        lastMessages.add("No messages");
        messageTimes.add("");

        profilePictures.add("https://cdn.guidingtech.com/media/assets/2019/10/_1200x630_crop_center-center_82_none/Launch-Chrome-Incognito-Mode-Featured.png?mtime=1570415817");
    }
}

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    //private static final String TAG = "MessagesAdapter";

    List<String> profilePictures;
    List<String> fullNames;
    List<String> lastMessages;
    List<String> messageTimes;
    Context messageContext;

    public MessagesAdapter(List<String> profilePictures, List<String> fullNames, List<String> lastMessages, List<String> messageTimes, Context messageContext) {
        this.profilePictures = profilePictures;
        this.fullNames = fullNames;
        this.lastMessages = lastMessages;
        this.messageTimes = messageTimes;
        this.messageContext = messageContext;
    }


    @NonNull
    @Override
    public MessagesAdapter.MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_messages_listitem, parent, false);
        MessagesViewHolder holder = new MessagesViewHolder(view);

        //Log.d(TAG, "MessagesViewHolder created");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessagesViewHolder holder, final int position) {
        //Log.d(TAG, "setting item " + position);

        Glide.with(messageContext)
                .asBitmap()
                .load(profilePictures.get(position))
                .into(holder.profilePicture);

        holder.fullName.setText(fullNames.get(position));
        holder.lastMessage.setText(lastMessages.get(position));
        holder.messageTime.setText(messageTimes.get(position));

        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(messageContext, fullNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fullNames.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicture;
        TextView fullName;
        TextView lastMessage;
        TextView messageTime;
        ConstraintLayout messageLayout;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.message_picture);
            fullName = itemView.findViewById(R.id.message_username);
            lastMessage = itemView.findViewById(R.id.last_message);
            messageTime = itemView.findViewById(R.id.message_time);
            messageLayout = itemView.findViewById(R.id.messages_layout);
        }
    }
}