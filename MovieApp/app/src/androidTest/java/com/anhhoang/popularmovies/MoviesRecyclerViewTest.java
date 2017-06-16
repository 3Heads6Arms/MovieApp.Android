package com.anhhoang.popularmovies;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by anh.hoang on 6/1/17.
 */

@RunWith(AndroidJUnit4.class)
public class MoviesRecyclerViewTest {
    @Rule
    public ActivityTestRule<MoviesActivity> mActivityTestRule = new ActivityTestRule<>(MoviesActivity.class);

    @Test
    public void onItemsClick_activityChanges() {
        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition(0, click()));

        // On click success next screen should have title
        onView(
                allOf(
                        instanceOf(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(startsWith(""))));
    }
}
