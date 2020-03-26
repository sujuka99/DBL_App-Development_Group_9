package com.example.bq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Books extends AppCompatActivity {

    TextView pageTitle;
    Button createListingBtn;

    //data
    private List<String> bookImages;
    private List<String> bookTitles;
    private List<String> bookPrices;
    private List<String> bookSellers;
    private List<String> bookTimestamps;
    private List<String> bookDistances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        initData();

        RecyclerView recyclerView = findViewById(R.id.recycle_books);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        BooksAdapter adapter = new BooksAdapter(bookImages, bookTitles, bookPrices, bookSellers, bookTimestamps, bookDistances,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pageTitle = findViewById(R.id.book_page);
        pageTitle.setText(MainActivity.major + " Books");

        createListingBtn = findViewById(R.id.button_create_listing);
        createListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Creating listing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        bookImages = new ArrayList<>();
        bookTitles = new ArrayList<>();
        bookPrices = new ArrayList<>();
        bookSellers = new ArrayList<>();
        bookTimestamps = new ArrayList<>();
        bookDistances = new ArrayList<>();

        bookImages.add("https://i.redd.it/khdhw14ui7o41.jpg");
        bookTitles.add("Mandarian Ducks for Dummies");
        bookPrices.add("$100.000");
        bookSellers.add("BirdWatcher34");
        bookTimestamps.add("37m ago");
        bookDistances.add("700m away");

        bookImages.add("https://upload.wikimedia.org/wikipedia/en/6/62/ArtOfComputerProgramming.jpg");
        bookTitles.add("The Art of Computer Programming");
        bookPrices.add("$60");
        bookSellers.add("CoderBoy74");
        bookTimestamps.add("1h ago");
        bookDistances.add("2km away");

        bookImages.add("https://images-na.ssl-images-amazon.com/images/I/51sRrwGcg3L._SX350_BO1,204,203,200_.jpg");
        bookTitles.add("Concrete Mathematics");
        bookPrices.add("$100");
        bookSellers.add("CoderBoy74");
        bookTimestamps.add("1h ago");
        bookDistances.add("2km away");

        bookImages.add("https://upload.wikimedia.org/wikipedia/en/thumb/4/41/Clrs3.jpeg/220px-Clrs3.jpeg");
        bookTitles.add("Introduction to Algorithms");
        bookPrices.add("$40");
        bookSellers.add("ComputerMan22");
        bookTimestamps.add("4h ago");
        bookDistances.add("3km away");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");

        bookImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png");
        bookTitles.add("<book title>");
        bookPrices.add("<price>");
        bookSellers.add("<seller>");
        bookTimestamps.add("<time>");
        bookDistances.add("<distance>");
    }
}

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {
    private static final String TAG = "BooksAdapter";

    private List<String> bookImages;
    private List<String> bookTitles;
    private List<String> bookPrices;
    private List<String> bookSellers;
    private List<String> bookTimestamps;
    private List<String> bookDistances;
    private Context bContext;

    public BooksAdapter(List<String> bookImages, List<String> bookTitles, List<String> bookPrices, List<String> bookSellers, List<String> bookTimestamps, List<String> bookDistances, Context bContext) {
        this.bookImages = bookImages;
        this.bookTitles = bookTitles;
        this.bookPrices = bookPrices;
        this.bookSellers = bookSellers;
        this.bookTimestamps = bookTimestamps;
        this.bookDistances = bookDistances;
        this.bContext = bContext;
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_books_listitem, parent, false);
        BooksViewHolder holder = new BooksViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, final int position) {
        Glide.with(bContext).asBitmap().load(bookImages.get(position)).into(holder.bookImage);

        holder.bookTitle.setText(bookTitles.get(position));
        holder.bookPrice.setText(bookPrices.get(position));
        holder.bookSeller.setText(bookSellers.get(position));
        holder.bookTimestamp.setText(bookTimestamps.get(position));
        holder.bookDistance.setText(bookDistances.get(position));

        holder.bookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(bContext, bookTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookImages.size();
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
