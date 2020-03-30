package com.example.bq.booktest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bq.R;
import com.example.bq.datatypes.BookData;
import com.example.bq.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;
    private HomeFragment parent;

    private ProgressBar bookBar;

    private List<BookData> bookData = new ArrayList<>();

    private BooksAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_books, container, false);

        parent = (HomeFragment) getParentFragment();

        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        bookBar = root.findViewById(R.id.booksProgress);

        loadDataInVM();

        initRecycleView(root);

        Button createListingBtn = root.findViewById(R.id.button_create_listing);
        createListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment fragment = (HomeFragment) getParentFragment();
                fragment.addBook();
            }
        });

        return root;
    }

    private void initRecycleView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recycle_books);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new BooksAdapter(bookData, this, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadDataInVM() {
        viewModel.loadBooksIntoViewModel(parent.major);

        final Observer<List<BookData>> bookDataObserver = new Observer<List<BookData>>() {
            @Override
            public void onChanged(final List<BookData> data) {
                bookData.clear();
                bookData.addAll(data);
                if (bookBar.getVisibility() != View.GONE) {
                    bookBar.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getBooks().observe(this, bookDataObserver);
    }

    public void loadBookDetails(int position) {
        parent.loadBookDetails(bookData.get(position));
    }
}


