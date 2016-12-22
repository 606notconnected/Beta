package com.example.asdf.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class loginTest {


    @Rule
    public ActivityTestRule<login> mActivityRule = new ActivityTestRule<>(
            login.class);

    @Test
    public void sayHello(){
        onView(withId(R.id.account)).perform(typeText("15505903134"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
    }

}