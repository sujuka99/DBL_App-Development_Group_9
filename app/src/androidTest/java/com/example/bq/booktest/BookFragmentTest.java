package com.example.bq.booktest;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class BookFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.beforeTests();

        //open books fragment
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popupBook)).perform(click());
        Thread.sleep(100);
    }

    @Test
    public void test_backButton() {
        pressBack();
        onView(withId(R.id.fragment_holder)).check(matches(isDisplayed()));
    }

}