package com.example.bq.booktest;

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
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.bq.R;
import com.example.bq.datatypes.BookData;
import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.FirebaseObserver;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class BookDetailsFragment extends Fragment implements FirebaseObserver {

    private BookData bookData;

    private BookViewModel viewModel;

    private Button deleteBook;

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

        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        try {
            bookData = new BookData((HashMap<String, String>) getArguments().getSerializable("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        Glide.with(getContext()).asBitmap().load("https://upload.wikimedia.org/wikipedia/commons/thumb" +
                "/f/f8/Question_mark_alternate.svg/1200px-Question_mark_alternate.svg.png")
                .into((ImageView) root.findViewById(R.id.bookImage));

        TextView title = root.findViewById(R.id.bookTitle);
        TextView author = root.findViewById(R.id.bookAuthor);
        TextView description = root.findViewById(R.id.bookDescription);
        TextView price = root.findViewById(R.id.bookPrice);
        TextView seller = root.findViewById(R.id.bookSeller);
        TextView distance = root.findViewById(R.id.bookDistance);

        deleteBook = root.findViewById(R.id.deleteBook);

        title.setText(bookData.title == null ? "No Title" : bookData.title);
        author.setText(bookData.author == null ? "No Author" : bookData.author);
        description.setText(bookData.description == null ? "No description" : bookData.description);
        price.setText(bookData.price == null ? "No price" : bookData.price);
        seller.setText(bookData.seller == null ? "No seller" : bookData.seller.split("-")[0]);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the user its profile
            }
        });

        // Edit to actually give a right distance!
        distance.setText(bookData.location == null ? "No location" : bookData.location);

        if (bookData.seller.split("-")[1].trim().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid().trim())) {
            initDeleteButton(false);
            return;
        }

        DataManager.getInstance().isAdmin(FirebaseAuth.getInstance().getCurrentUser().getUid(), this);
    }

    private void initDeleteButton(boolean admin) {
        deleteBook.setVisibility(View.VISIBLE);
        deleteBook.setText(admin == true ? "Delete" : "Unregister");
        final BookDetailsFragment fragment = this;
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.removeBook(bookData, fragment);
            }
        });
    }

    @Override
    public void notifyOfCallback(Object obj) {
        if (obj instanceof HashMap) {
            HashMap<String, Object> result = (HashMap<String, Object>) obj;
            if (result.get("action") == "removeBook") {
                if ((boolean) result.get("result")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully removed the book!", Toast.LENGTH_SHORT).show();
                    HomeFragment parent = (HomeFragment) getParentFragment();
                    parent.getChildFragmentManager().popBackStackImmediate();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Could not remove the book!", Toast.LENGTH_SHORT).show();
            } else if (result.get("action") == "isAdmin") {
                if ((boolean) result.get("result")) {
                    initDeleteButton(true);
                }
            }
        }
    }
}
