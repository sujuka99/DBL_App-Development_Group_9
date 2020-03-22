package com.example.bq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Books extends AppCompatActivity {

    TextView textView;
    //Activity_books variables
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mBook_Seller = new ArrayList<>();
    private ArrayList<String> mImageURL = new ArrayList<>();
    private ArrayList<String> mTime_Posted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        textView = findViewById(R.id.textView4);
        textView.setText(MainActivity.major);

        initRecyclerView();
        initImageBitmaps();
    }

    //image processing activity_books
    private void initImageBitmaps() {

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");

        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

        mImageURL.add("https://i.redd.it/j6myfqglup501.jpg");
        mBook_Seller.add("Seller_name");
        mPrice.add("€25,00");
        mTime_Posted.add("5 minutes ago");
        mTitle.add("Title");

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycle_books);
        BooksAdapter adapter = new BooksAdapter(this, mImageURL, mTitle, mBook_Seller, mTime_Posted, mPrice);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mBook_Seller = new ArrayList<>();
    private ArrayList<String> mTime_Posted = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private Context bContext; //context might be wrong

    public BooksAdapter(Context bContext, ArrayList<String> mImage, ArrayList<String> mTitle, ArrayList<String> mBook_Seller, ArrayList<String> mTime_Posted, ArrayList<String> mPrice) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mBook_Seller = mBook_Seller;
        this.mTime_Posted = mTime_Posted;
        this.mPrice = mPrice;
        this.bContext = bContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_books_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(bContext)
                .asBitmap()
                .load(mImage.get(position))
                .into(holder.book_image);


        holder.book_seller.setText(mBook_Seller.get(position));
        holder.price.setText(mPrice.get(position));
        holder.title.setText(mTitle.get(position));
        holder.time_posted.setText(mTime_Posted.get(position));

        holder.books_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImage.get(position));

                Toast.makeText(bContext, mImage.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView book_image;
        TextView title;
        TextView book_seller;
        TextView time_posted;
        TextView price;
        RelativeLayout books_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_image = itemView.findViewById(R.id.book_image);
            title = itemView.findViewById(R.id.title);
            book_seller = itemView.findViewById(R.id.book_seller);
            time_posted = itemView.findViewById(R.id.time_posted);
            price = itemView.findViewById(R.id.price);
            books_layout = itemView.findViewById(R.id.books_layout);

        }
    }
}