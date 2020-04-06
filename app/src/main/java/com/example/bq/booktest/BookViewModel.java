package com.example.bq.booktest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.MainActivity;
import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookViewModel extends ViewModel {

    private MutableLiveData<List<BookData>> books;

    public BookViewModel() {
        books = new MutableLiveData<>();
        books.setValue(new ArrayList<BookData>());
    }

    public void loadBooksIntoViewModel(String study, final FirebaseObserver observer) {
        if (study == null) {
            return;
        }
        DataManager.getInstance().getBooks(study, 0, MainActivity.userLocation, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if (obj instanceof ArrayList) {
                    ArrayList<BookData> data = (ArrayList<BookData>) obj;
                    books.setValue(data);
                    observer.notifyOfCallback(true);
                }
            }
        });
    }

    public LiveData<List<BookData>> getBooks() {
        return books;
    }

    public void addBook(final BookData data, final FirebaseObserver observer) {
        final List<BookData> bookList = books.getValue();
        DataManager.getInstance().addBook(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if ((boolean) obj) {
                    bookList.add(data);
                    books.setValue(bookList);
                }
                observer.notifyOfCallback(obj);
            }
        });
    }

    public void removeBook(BookData data, final FirebaseObserver observer) {
        DataManager.getInstance().deleteBook(data.study, data.id, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("action", "removeBook");
                result.put("result", obj);
                observer.notifyOfCallback(result);
            }
        });
    }
}
