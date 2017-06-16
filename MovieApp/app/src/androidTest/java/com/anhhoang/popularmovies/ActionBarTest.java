package com.anhhoang.popularmovies;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActionBarTest {
    @Rule
    public ActivityTestRule<MoviesActivity> mActivityTestRule = new ActivityTestRule<>(MoviesActivity.class);


    @Test
    public void actionBarButtons_onClick() throws Exception {
        openActionBarOverflowOrOptionsMenu(mActivityTestRule.getActivity());
        onView(withText("Popular"))
                .perform(click());

        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition(0, click()));
    }

    @Test
    public void actionBarButtons_upClick() {
        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition(0, click()));

        // upon click detail activity should be opened and available for navigate up
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());

        onView(withId(R.id.rv_reviews))
                .check(doesNotExist());
    }
}
