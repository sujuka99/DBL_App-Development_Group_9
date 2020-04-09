package com.example.bq.questiontest;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.beforeTests();

        //open books fragment
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popupQuestion)).perform(click());
        Thread.sleep(100);
    }

    @Test
    public void test_isFragmentInView() {
        onView(withId(R.id.questions_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.recycle_questions)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create_question)).check(matches(isDisplayed()));
    }

    //Should change when all text comes from strings.xml
    @Test
    public void test_properTextEntries() {
        onView(withId(R.id.button_create_question)).check(matches(withText("Create question")));
    }

    @Test
    public void test_navQuestionDetailsFragment() throws InterruptedException {
        Thread.sleep(1000);
        try {
            onView(withId(R.id.recycle_questions)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.question_layout)).check(matches(isDisplayed()));
        } catch (PerformException pe) {
            //no question in viewholder, assume the worst
            assertTrue(false);
        }
    }

    @Test
    public void test_navAddBookFragment() {
        onView(withId(R.id.button_create_question)).perform(click());
        onView(withId(R.id.addquestion_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_backButton() {
        pressBack();
        onView(withId(R.id.fragment_holder)).check(matches(isDisplayed()));
    }

}