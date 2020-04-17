package com.example.bq.booktest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bq.LoginActivity;
import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.UUID;


public class AddBookFragment extends Fragment {

    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookPrice;

    private BookViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addbook, container, false);

        // Load the ViewModel which is shared between all book fragments
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Create references to all components
        initializeComponents(root);

        return root;
    }

    /**
     * Initialize the components for input fields and the register button which allow users to
     * register a new book
     *
     * @param root Root view in which the components can be found
     */
    private void initializeComponents(View root) {
        bookAuthor = root.findViewById(R.id.bookAuthor);
        bookDescription = root.findViewById(R.id.bookDescription);
        bookPrice = root.findViewById(R.id.bookPrice);
        bookTitle = root.findViewById(R.id.bookTitle);

        Button registerBook = root.findViewById(R.id.registerBook);

        registerBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBook();
            }
        });
    }

    /**
     * Register a book for sale, checks all input fields for validity of the input and throws errors
     * in the EditText fields if not.
     * If all fields are valid, a new BookData object is created and the ViewModel is called to handle
     * adding the new book
     */
    private void registerBook() {
        BookData data = new BookData();

        // Check for empty fields
        if (TextUtils.isEmpty(bookTitle.getText().toString())) {
            bookTitle.setError(getString(R.string.ErrorBookTitle));
            return;
        }
        if (TextUtils.isEmpty(bookAuthor.getText().toString())) {
            bookAuthor.setError(getString(R.string.ErrorBookAuthor));
            return;
        }
        if (TextUtils.isEmpty(bookDescription.getText().toString())) {
            bookDescription.setError(getString(R.string.ErrorBookDescription));
            return;
        }
        if (TextUtils.isEmpty(bookPrice.getText().toString())) {
            bookPrice.setError(getString(R.string.ErrorBookPrice));
            return;
        }

        // Now that all fields are filled in, make sure that the user is actually is logged in, if
        // not, return the user to the Login Screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.ErrorNotLoggedIn,
                    Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            getActivity().finish();
        }

        // After all checks are done, fill in all information into the BookData object
        HomeFragment parent = (HomeFragment) getParentFragment();
        assert parent != null;

        data.title = bookTitle.getText().toString();
        data.author = bookAuthor.getText().toString();
        data.study = parent.major;
        data.price = bookPrice.getText().toString();
        data.description = bookDescription.getText().toString();
        // Store the account that is selling as 'displayName'-'id' for ease of reference
        data.seller = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                + "-"
                + FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Generate a new id for this book, make it have no - as ids of firebase do not have them
        // either. (Stylistic choice)
        data.id = UUID.randomUUID().toString().replaceAll("-", "");
        // Get the user its location
        Location loc = MainActivity.userLocation;
        // And set the location accordingly
        data.location = loc == null ? "0:0" : loc.getLatitude() + ":" + loc.getLongitude();
        // Add a timestamp
        data.timeStamp = Long.toString(Calendar.getInstance().getTimeInMillis());

        // Finally, let the ViewModel register the book and wait for the callback
        viewModel.addBook(data, new ViewModelCaller() {
            @Override
            public void callback(Object obj) {
                assert (getActivity() != null);
                Context appContext = getActivity().getApplicationContext();
                assert (appContext != null);

                // Our callback can be either a string or true, if it is a string, then it must be an error
                // Otherwise it was successful and we close the fragment
                if (obj instanceof String) {
                    Toast.makeText(appContext, (String) obj, Toast.LENGTH_SHORT).show();
                } else if ((boolean) obj) {
                    Toast.makeText(appContext, R.string.MessageSuccessAddBook, Toast.LENGTH_SHORT).show();
                    HomeFragment parent = (HomeFragment) getParentFragment();
                    assert (parent != null);
                    parent.getChildFragmentManager().popBackStackImmediate();
                }
            }
        });
    }
}
