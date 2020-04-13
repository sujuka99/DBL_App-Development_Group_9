package com.example.bq.questiontest;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class AddQuestionFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.navToHome();
        QuestionFragmentTest.navToQuestions();
        onView(withId(R.id.button_create_question)).perform(click());
    }

    @Test
    public void test_visibilityOfLayoutElements() throws InterruptedException {
        onView(withId(R.id.addQuestionBody)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionPage)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestionTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void test_questionRegister() throws InterruptedException {
        //add test question
        onView(withId(R.id.addQuestionTitle)).perform(typeText(("test0")));
        onView(withId(R.id.addQuestionBody)).perform(typeText(("bodytest0")));
        onView(withId(R.id.addQuestionButton)).perform(click());

        //wait for question to load
        Thread.sleep(1500);

        //check is the question is displayed
        onView(withText("test0")).check(matches(isDisplayed()));

        //remove question
        onView(withText("test0")).perform(click());
        onView(withId(R.id.questionDetailButton)).perform(click());
    }

    @Test
    public void test_removeQuestion() throws InterruptedException {
        //add test question
        onView(withId(R.id.addQuestionTitle)).perform(typeText(("test0")));
        onView(withId(R.id.addQuestionBody)).perform(typeText("bodytest0"));
        onView(withId(R.id.addQuestionButton)).perform(click());

        //wait for the question to load
        Thread.sleep(1500);

        //check if the question is displayed
        onView(withText("test0")).check(matches(isDisplayed()));

        //remove question
        onView(withText("test0")).perform(click());
        onView(withId(R.id.questionDetailButton)).perform(click());

        //wait for the question to be removed
        Thread.sleep(1500);

        //check if question is removed
        onView(withText("test0")).check(doesNotExist());



    }
}