package com.example.bq.booktest;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class BookDetailsFragment extends Fragment {

    private BookData bookData;

    private BookViewModel viewModel;

    private Button deleteBook;

    private HomeFragment parent;

    public static BookDetailsFragment newInstance(BookData data) {
        Bundle args = new Bundle();
        args.putSerializable("data", data.toMap());
        BookDetailsFragment f = new BookDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookdetails, container, false);

        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        parent = (HomeFragment) getParentFragment();

        try {
            assert getArguments() != null;
            Object data = getArguments().getSerializable("data");
            assert data != null;
            bookData = new BookData((HashMap<String, String>) data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents(root);
        return root;
    }

    private void initComponents(View root) {
        Glide.with(getContext()).asBitmap().load("https://upload.wikimedia.org/wikipedia/commons/" +
                "thumb" +
                "/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png")
                .into((ImageView) root.findViewById(R.id.bookImage));

        TextView title = root.findViewById(R.id.bookTitle);
        TextView author = root.findViewById(R.id.bookAuthor);
        TextView description = root.findViewById(R.id.bookDescription);
        TextView price = root.findViewById(R.id.bookPrice);
        TextView seller = root.findViewById(R.id.bookSeller);
        TextView distance = root.findViewById(R.id.bookDistance);
        TextView timeStamp = root.findViewById(R.id.bookTimeStamp);

        deleteBook = root.findViewById(R.id.deleteBook);

        title.setText(bookData.title == null ? "No Title" : bookData.title);
        author.setText(bookData.author == null ? "No Author" : bookData.author);
        description.setText(bookData.description == null ? "No description" : bookData.description);
        price.setText(bookData.price == null ? "No price" : bookData.price);
        seller.setText(bookData.seller == null ? "No seller" : bookData.seller.split("-")[0]);
        timeStamp.setText(TimeStamp.toTime(bookData.timeStamp));

        String[] longLat = bookData.location.split(":");
        Location location = new Location("");
        location.setLatitude(Float.parseFloat(longLat[0].trim()));
        location.setLongitude(Float.parseFloat(longLat[1].trim()));

        distance.setText("" + Math.round(MainActivity.userLocation.distanceTo(location)) + "m");

        if (bookData.seller.split("-")[1].trim().equalsIgnoreCase(FirebaseAuth.getInstance().
                getCurrentUser().getUid().trim())) {
            initDeleteButton(false);
        } else if (MainActivity.isAdmin) {
            initDeleteButton(true);
        }
    }

    private void initDeleteButton(boolean admin) {
        deleteBook.setVisibility(View.VISIBLE);
        deleteBook.setText(admin ? "Delete" : "Unregister");
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.removeBook(bookData, new ViewModelCaller() {
                    @Override
                    public void callback(Object obj) {
                        if (obj instanceof String) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    (String) obj, Toast.LENGTH_SHORT).show();

                        } else if ((boolean) obj) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Successfully removed the book!", Toast.LENGTH_SHORT).show();
                            parent.getChildFragmentManager().popBackStackImmediate();
                        }
                    }
                });
            }
        });
    }
}
