package com.example.bq.questiontest;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class QuestionFragmentTest {

    @Before
    public static void beforeTests() throws InterruptedException {
        MainActivityTest.beforeTests();

        //go to questions
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popupQuestion)).perform(click());
        Thread.sleep(100);
    }

}