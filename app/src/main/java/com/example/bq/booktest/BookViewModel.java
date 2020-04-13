package com.example.bq.booktest;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.MainActivity;
import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookViewModel extends ViewModel {

    private MutableLiveData<List<BookData>> books;

    public BookViewModel() {
        books = new MutableLiveData<>();
        books.setValue(new ArrayList<BookData>());
    }

    /**
     * Signal the view model to load all books of the specified study into the books object
     *
     * @param study Name of the study under which the books are requested
     */
    void loadBooksIntoViewModel(@Nullable String study, final ViewModelCaller caller) {
        // If the study is null, there are no books so return and do nothing
        if (study == null) {
            return;
        }
        // Otherwise, request the books from the firebase server
        DataManager.getInstance().getBooks(study, MainActivity.userLocation, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether our callback is indeed created by the getBooks function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_GET_BOOKS)) {
                    // Get the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    if ((boolean) response.get("success")) {
                        // If the request was handled sucessfully, we can now obtain the result
                        // Which in this case is a List of HashMaps
                        List<HashMap<String, String>> result = (List<HashMap<String, String>>) response.get("result");
                        // Create a new List of BookData
                        List<BookData> data = new ArrayList<>();
                        assert result != null;
                        // And for every HashMap in our result, convert it to a bookdata object
                        // and add it to our list of BookData
                        for (HashMap<String, String> book : result) {
                            data.add(new BookData(book));
                        }
                        // Finally we update our mutable live data with the newly obtained data
                        books.setValue(data);
                        // Let the BookFragment know that the books were loaded
                        caller.callback(true);
                    } else {
                        // Let the caller know we could not load the books
                        caller.callback(callback.get("error"));
                    }
                }
            }
        });
    }

    /**
     * Get a reference to our MutableLiveData object such that we can add observers
     *
     * @return Our mutable live data to which we want a reference
     */
    LiveData<List<BookData>> getBooks() {
        return books;
    }

    /**
     * Add a book to the ViewModel and to the firebase database
     *
     * @param data A {@link BookData} object containing all the data of the new book
     */
    void addBook(final BookData data, final ViewModelCaller caller) {
        // Get a reference to our current books
        final List<BookData> bookList = books.getValue();
        // Then tell the DataManager to add our book
        DataManager.getInstance().addBook(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether our callback is indeed created by the addBook function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_ADD_BOOK)) {
                    // If so, we can get the result of our
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    if ((boolean) response.get("success")) {
                        bookList.add(0, data);
                        books.setValue(bookList);
                        // Let the AddBookFragment know that the book was added successfully
                        caller.callback(true);
                    } else {
                        // Let the caller know that the book was not added
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }

    /**
     * Remove a book from the ViewModel and from the firebase database
     *
     * @param data The {@link BookData} object containing the data of the book to be deleted
     */
    void removeBook(BookData data, final ViewModelCaller caller) {
        DataManager.getInstance().deleteBook(data.study, data.id, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_DELETE_BOOK)) {
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    if ((boolean) response.get("success")) {
                        // Let the caller know it was successful
                        caller.callback(true);
                    } else {
                        // Let bookdetailsfragment know it was a failure and give the error
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }
}
