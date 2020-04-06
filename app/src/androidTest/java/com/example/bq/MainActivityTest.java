package com.example.bq;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoActivityResumedException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Before
    public void beforeTests() {
        ActivityScenario<Login> mLoginActivityScenario = ActivityScenario.launch(Login.class);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //enter login details
            onView(withId(R.id.Email)).perform(typeText("test@test.test"));
            onView(withId(R.id.password)).perform(typeText("test123"));

            //click login
            onView(withId(R.id.loginBtn)).perform(click());
        }
    }

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.fragment_holder)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_home_studies)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBCS)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBDS)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBAM)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBAP)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBBE)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBEE)).check(matches(isDisplayed()));
    }

    @Test
    public void test_backButton() {
        try {
            pressBack();
            assertTrue(false); //app not killed, not what we want
        } catch (NoActivityResumedException e) { //app killed
            assertTrue(true); //this is desired outcome!
        }
    }
}