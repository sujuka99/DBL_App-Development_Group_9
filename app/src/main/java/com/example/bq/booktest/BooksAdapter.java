package com.example.bq.booktest;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.datatypes.BookData;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {

    private String defaultLink = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png";

    private List<BookData> bookData;

    private BookFragment fragment;
    private Context bContext;

    public BooksAdapter(List<BookData> data, BookFragment fragment, Context bContext) {
        this.bookData = data;
        this.bContext = bContext;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BooksAdapter.BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_books_listitem, parent, false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.BooksViewHolder holder, final int position) {
        Glide.with(bContext).asBitmap().load(defaultLink).into(holder.bookImage);

        BookData data = bookData.get(position);
        holder.bookTitle.setText(data.title);
        holder.bookPrice.setText(data.price);
        holder.bookSeller.setText(data.seller.split("-")[0]);
        holder.bookTimestamp.setText("No Time");
        String[] longLat = data.location.split(":");
        Location location = new Location("");
        location.setLatitude(Float.parseFloat(longLat[0].trim()));
        location.setLongitude(Float.parseFloat(longLat[1].trim()));

        holder.bookDistance.setText("" + Math.round(MainActivity.userLocation.distanceTo(location)) + "m");

        holder.bookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.loadBookDetails(position);
                Toast.makeText(v.getContext(), "Load book details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookData.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        TextView bookTitle;
        TextView bookPrice;
        TextView bookSeller;
        TextView bookTimestamp;
        TextView bookDistance;
        ConstraintLayout bookLayout;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookSeller = itemView.findViewById(R.id.book_seller);
            bookTimestamp = itemView.findViewById(R.id.book_timestamp);
            bookDistance = itemView.findViewById(R.id.book_distance);
            bookLayout = itemView.findViewById(R.id.book_layout);
        }
    }
}
