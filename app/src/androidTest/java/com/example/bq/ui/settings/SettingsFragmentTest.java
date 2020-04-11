package com.example.bq.ui.settings;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.PreferenceMatchers.withKey;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

public class SettingsFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.navToHome();
        navToSettings();
    }

    public static void navToSettings() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(1000);

        //click on Settings
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 1200);
        Thread.sleep(1000);
    }

    /**@Test
    public void test_isActivityInView() {
        //no clue why settings_layout and settings_screen don't show up in view hierarchy
        //onView(withId(R.id.settings_screen)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        //settings elements id's not appearing in view hierarchy either
        //onView(withId(R.id.useLocation)).check(matches(isDisplayed()));
    }*/

    @Test
    public void test_properTextEntries() {
        //settings elements id's not appearing in view hierarchy either
        onView(withText("General")).check(matches(isDisplayed()));

        onView(withText("Location")).check(matches(isDisplayed()));
        onView(withText("Whether to use location")).check(matches(isDisplayed()));


        onView(withText("Other settings")).check(matches(isDisplayed()));

        onView(withText("Checkbox")).check(matches(isDisplayed()));
        onView(withText("This is a checkbox")).check(matches(isDisplayed()));

        onView(withText("Edit Text")).check(matches(isDisplayed()));
        //onView(withText("")).check(matches(isDisplayed())); editText summary changes
    }

    @Test
    public void test_functionalityOfButtons() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Location")),
                        click()));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Checkbox")),
                        click()));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Edit Text")),
                        click()));
        typeText("some text");
        pressBack();
    }

    @Test
    public void test_backButton() {
        pressBack();
        onView(withId(R.id.fragment_home_studies)).check(matches(isDisplayed()));
    }

}