package com.example.bq;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoActivityResumedException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    int DRAWER_ANIMATION_TIMEOUT = 1000;

    @Before
    public static void beforeTests() throws InterruptedException {
        ActivityScenario<LoginActivity> mLoginActivityScenario = ActivityScenario.launch(LoginActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //enter login details
            onView(withId(R.id.Email)).perform(typeText("test@test.test"));
            onView(withId(R.id.password)).perform(typeText("test123"));

            //hide keyboard
            pressBack();

            //click login
            onView(withId(R.id.loginBtn)).perform(click());

            //time for login
            Thread.sleep(1000);
        }
    }

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() throws InterruptedException {
        onView(withId(R.id.fragment_holder)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_home_studies)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBCS)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBDS)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBAM)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBAP)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBBE)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBEE)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        // Open the action bar overflow or options menu (depending if the device has or not a hardware menu button.)
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(withText(R.string.action_logout)).check(matches(isDisplayed()));
        if (MainActivity.isAdmin) {
            onView(withText(R.string.admin)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void test_properTextEntries() {
        onView(withId(R.id.buttonBCS)).check(matches(withText(R.string.home_button_BCS)));
        onView(withId(R.id.buttonBDS)).check(matches(withText(R.string.home_button_BDS)));
        onView(withId(R.id.buttonBAM)).check(matches(withText(R.string.home_button_BAM)));
        onView(withId(R.id.buttonBAP)).check(matches(withText(R.string.home_button_BAP)));
        onView(withId(R.id.buttonBBE)).check(matches(withText(R.string.home_button_BBE)));
        onView(withId(R.id.buttonBEE)).check(matches(withText(R.string.home_button_BEE)));
    }

    @Test
    public void test_popup() {
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popup_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.popupBook)).check(matches(isDisplayed()));
        onView(withId(R.id.popupQuestion)).check(matches(isDisplayed()));
        onView(withId(R.id.popupText)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navBooks() throws InterruptedException {
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popupBook)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.book_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navQuestions() throws InterruptedException {
        onView(withId(R.id.buttonBCS)).perform(click());
        onView(withId(R.id.popupQuestion)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.questions_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navMenuHome() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(DRAWER_ANIMATION_TIMEOUT);

        //click on Home
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 550);
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navMenuMyProfile() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(DRAWER_ANIMATION_TIMEOUT);

        //click on My Profile
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 675);
        onView(withId(R.id.profile_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navMenuMessages() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(DRAWER_ANIMATION_TIMEOUT);

        //click on Messages
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 800);
        onView(withId(R.id.messages_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navMenuHelp() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(DRAWER_ANIMATION_TIMEOUT);

        //click on Help
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 925);
        onView(withId(R.id.help_layout)).check(matches(isDisplayed()));
    }
    @Test
    public void test_navMenuSettings() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(DRAWER_ANIMATION_TIMEOUT);

        //click on Settings
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 1200);
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        //onView(withId(R.id.recycler_view))
        //        .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Location")),
        //                click()));

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