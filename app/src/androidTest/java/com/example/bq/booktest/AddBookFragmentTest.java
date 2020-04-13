package com.example.bq.booktest;

import androidx.test.espresso.matcher.ViewMatchers;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class AddBookFragmentTest {

    @Before
    public void  BeforeTests() throws InterruptedException{
        MainActivityTest.navToHome();
        BookFragmentTest.navToBooks();
        onView(withId(R.id.button_create_listing)).perform(click());
    }

    @Test
    public void test_visibilityOfLayoutElements() throws InterruptedException {
        onView(withId(R.id.bookImage)).check(matches(isDisplayed()));
        onView(withId(R.id.bookTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.bookPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.bookDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.bookAuthor)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerBooks() throws InterruptedException {
        //add dummy text
        onView(withId(R.id.bookTitle)).perform(typeText("test0"));
        onView(withId(R.id.bookAuthor)).perform(typeText("2"));
        onView(withId(R.id.bookDescription)).perform(typeText("3"));
        //minimize keyboard
        pressBack();
        onView(withId(R.id.bookPrice)).perform(typeText("4"));

        //minimize keyboard
        pressBack();

        //register book
        onView(withId(R.id.registerBook)).perform(click());

        //wait for the book to load
        Thread.sleep(2000);

        //check is the book is added
        onView(withText("test0")).check(matches(isDisplayed()));
        onView(withText("test0")).perform(click());

        //remove book
        onView(withText("test0")).perform(click());
        onView(withId(R.id.deleteBook)).perform(click());
    }


    @Test
    public void test_removeBook() throws InterruptedException {
        //add dummy text
        onView(withId(R.id.bookTitle)).perform(typeText("test0"));
        onView(withId(R.id.bookAuthor)).perform(typeText("2"));
        onView(withId(R.id.bookDescription)).perform(typeText("3"));
        //minimize keyboard
        pressBack();
        onView(withId(R.id.bookPrice)).perform(typeText("4"));

        //minimize keyboard
        pressBack();

        //register book
        onView(withId(R.id.registerBook)).perform(click());

        //wait for the book to load
        Thread.sleep(1500);

        //check is the book is added
        onView(withText("test0")).check(matches(isDisplayed()));
        onView(withText("test0")).perform(click());

        //remove book
        onView(withText("test0")).perform(click());
        onView(withId(R.id.deleteBook)).perform(click());

        //wait for the book to be removed
        Thread.sleep(1500);

        //check if the book is removed
        onView(withText("text0")).check(doesNotExist());
    }
}