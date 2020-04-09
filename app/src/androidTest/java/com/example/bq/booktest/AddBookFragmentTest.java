package com.example.bq.booktest;

import androidx.test.espresso.matcher.ViewMatchers;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class AddBookFragmentTest {

    @Before
    public void  BeforeTests() throws InterruptedException{
        MainActivityTest.beforeTests();
        BookFragmentTest.beforeTests();
        onView(withId(R.id.button_create_listing)).perform(click());
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.bookImage)).check(matches(isDisplayed()));
        onView(withId(R.id.bookTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.bookPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.bookDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.bookAuthor)).check(matches(isDisplayed()));
    }

}