package com.example.bq.questiontest;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class AddQuestionFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.beforeTests();
        QuestionFragmentTest.beforeTests();
        onView(withId(R.id.button_create_question)).perform(click());
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.addQuestionBody)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionPage)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionTitle)).check(matches(isDisplayed()));
    }

}