package com.example.bq.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bq.Questions;
import com.example.bq.R;
import com.example.bq.booktest.AddBookFragment;
import com.example.bq.booktest.BookDetailsFragment;
import com.example.bq.booktest.BookFragment;
import com.example.bq.datatypes.BookData;
import com.example.bq.datatypes.QuestionData;
import com.example.bq.questiontest.AddQuestionFragment;
import com.example.bq.questiontest.QuestionDetails;
import com.example.bq.questiontest.QuestionFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HomeFragment extends Fragment {

    public String major;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loadChildFragment();
        return root;
    }

    // Used to make sure that on rotate the right child fragment is maintained
    public void loadChildFragment() {
        if (getChildFragmentManager().getFragments().size() > 0) {
            return;
        }
        Fragment homeStudies = new HomeStudies();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, homeStudies).commit();
    }

    public void onButtonShowPopupWindowClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        popupView.findViewById(R.id.popupBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onBookClick(v);
            }
        });

        popupView.findViewById(R.id.popupQuestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onQuestionsClick(v);
            }
        });
    }

    public void loadBooks(boolean addToBackStack) {
        BookFragment fragment = new BookFragment();
        if (addToBackStack) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
            return;
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
    }

    public void loadQuestions(boolean addToBackStack) {
        QuestionFragment fragment = new QuestionFragment();
        if (addToBackStack) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
            return;
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
    }

    public void onBookClick(View v) {
        loadBooks(true);
    }

    public void onQuestionsClick(View v) {
        loadQuestions(true);
    }

    public void addBook() {
        AddBookFragment fragment = new AddBookFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
    }

    public void addQuestion(){
        AddQuestionFragment fragment = new AddQuestionFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
    }

    public void loadBookDetails(BookData data) {
        BookDetailsFragment fragment = BookDetailsFragment.newInstance(data);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
    }

    public void loadQuestionDetails(QuestionData data) {
        QuestionDetails fragment = QuestionDetails.newInstance(data);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
    }
}