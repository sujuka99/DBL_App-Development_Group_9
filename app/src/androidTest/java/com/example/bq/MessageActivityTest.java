package com.example.bq;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoActivityResumedException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageActivityTest {

    @Before
    public void beforeTests() throws InterruptedException {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            ActivityScenario<LoginActivity> mLoginActivityScenario = ActivityScenario.launch(LoginActivity.class);
        }
        MainActivityTest.navToHome();
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(1000);

        //click on Messages
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 800);
        //time to load messages page
        Thread.sleep(1000);
    }

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.messages_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() throws InterruptedException {

        onView(withId(R.id.messages_recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.text_messages)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navMessageActivity() throws InterruptedException {
        Thread.sleep(6000);
        try {
            onView(withId(R.id.messages_recyclerview)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Receiver"))));

            onView(withId(R.id.messages_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Receiver")),click()));
            //give it a second (probably not necessary)
            Thread.sleep(1000);
            onView(withId(R.id.relativeLayout)).check(matches(isDisplayed()));

            //generate a random message
            int MESSAGE_LENGHT = 20;
            String message = "";
            Random rand = new Random();

            for (int i = 0; i < MESSAGE_LENGHT; i++) {
                message += Integer.toString(rand.nextInt(26) + 1, 36); //alphanumeric ?
            }

            message += "test";




            onView(withId(R.id.text_send)).perform(typeText(message));
            onView(withId(R.id.btn_send)).perform(click());


            FirebaseAuth.getInstance().signOut();
            ActivityScenario<LoginActivity> mLoginActivityScenario = ActivityScenario.launch(LoginActivity.class);

            onView(withId(R.id.Email)).perform(typeText("receiver@r.com"));
            onView(withId(R.id.password)).perform(typeText("receiver"));

            //hide keyboard
            pressBack();

            //click login
            onView(withId(R.id.loginBtn)).perform(click());

            //time for login
            Thread.sleep(1000);

            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
            Thread.sleep(1000);

            //click on Messages
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 800);
            //time to load messages page
            Thread.sleep(1000);

            onView(withId(R.id.messages_recyclerview)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Test Account"))));

            onView(withId(R.id.messages_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test Account")),click()));
            //give it a second (probably not necessary)
            Thread.sleep(1000);
            onView(withId(R.id.relativeLayout)).check(matches(isDisplayed()));

            onView(withText(message)).check(matches(isDisplayed()));






        } catch (PerformException pe) {
            //no users in viewholder, assume the worst
            assertTrue(false);
        }
    }
}
