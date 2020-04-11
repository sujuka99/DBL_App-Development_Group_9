package com.example.bq.ui.help;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.bq.MainActivity;
import com.example.bq.MainActivityTest;
import com.example.bq.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class HelpFragmentTest {

    @Before
    public void beforeTests() throws InterruptedException {
        MainActivityTest.navToHome();
        navToHelp();
    }

    public static void navToHelp() throws InterruptedException {
        //click on drawer
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(70, 140);
        Thread.sleep(1000);

        //click on Help
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(250, 925);
        Thread.sleep(1000);
    }

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.help_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibilityOfLayoutElements() {
        onView(withId(R.id.text_help)).check(matches(isDisplayed()));
    }

    @Test
    public void test_properTextEntries() {
        onView(withId(R.id.text_help)).check(matches(withText(R.string.help_content)));
    }

    @Test
    public void test_backButton() {
        pressBack();
        onView(withId(R.id.fragment_home_studies)).check(matches(isDisplayed()));
    }
}