package com.example.bq;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoActivityResumedException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterTest {

    @Rule
    public ActivityScenarioRule<Register> registerActivityScenarioRule = new ActivityScenarioRule<>(Register.class);

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.register_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.fullName)).check(matches(isDisplayed()));
        onView(withId(R.id.Email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.createText)).check(matches(isDisplayed()));
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    //Should change when all text comes from strings.xml
    @Test
    public void test_properTextEntries() {
        onView(withId(R.id.textView)).check(matches(withText("AppName")));
        onView(withId(R.id.fullName)).check(matches(withHint("Full Name")));
        onView(withId(R.id.Email)).check(matches(withHint("Email")));
        onView(withId(R.id.password)).check(matches(withHint("Password")));
        onView(withId(R.id.loginBtn)).check(matches(withText("Register")));
        onView(withId(R.id.createText)).check(matches(withText("Login")));
    }

    @Test
    public void test_navLoginScreen() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            ActivityScenario<Register> mRegisterActivityScenario = ActivityScenario.launch(Register.class);
        }

        onView(withId(R.id.createText)).perform(click());
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerSequenceSuccess() throws InterruptedException {
        //generate random string of length 20 to avoid duplications.
        int EMAIL_LENGTH = 20;
        String email = "";
        Random rand = new Random();

        for (int i = 0; i < EMAIL_LENGTH; i++) {
            email += Integer.toString(rand.nextInt(26) + 1, 36); //alphanumeric ?
        }

        email += "@test.test";

        //enter details
        onView(withId(R.id.fullName)).perform(typeText("Register Test"));
        onView(withId(R.id.Email)).perform(typeText(email));
        onView(withId(R.id.password)).perform(typeText("any_password"));

        //click register
        onView(withId(R.id.loginBtn)).perform(click());

        //make sure button disappears and progress bar appears
        onView(withId(R.id.loginBtn)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //give app time to reach server to register --- imperfect, server can take longer
        Thread.sleep(1000);

        //check whether we reached the home page
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_registerSequenceFail() throws InterruptedException {
        //enter details
        onView(withId(R.id.fullName)).perform(typeText("Register Test"));
        onView(withId(R.id.Email)).perform(typeText("test@test.test")); //taken
        onView(withId(R.id.password)).perform(typeText("any_password"));

        //click register
        onView(withId(R.id.loginBtn)).perform(click());

        //make sure button disappears and progress bar appears
        onView(withId(R.id.loginBtn)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //give app time to reach server to register --- imperfect, server can take longer
        Thread.sleep(1000);

        //check that we are still on the register page and that the button has reappeared
        onView(withId(R.id.register_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.loginBtn)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
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