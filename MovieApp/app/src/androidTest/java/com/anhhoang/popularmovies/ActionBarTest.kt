package com.anhhoang.popularmovies

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ActionBarTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(MoviesActivity::class.java)


    @Test
    @Throws(Exception::class)
    fun actionBarButtons_onClick() {
        openActionBarOverflowOrOptionsMenu(mActivityTestRule.activity)
        onView(withText("Popular"))
                .perform(click())

        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    @Test
    fun actionBarButtons_upClick() {
        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // upon click detail activity should be opened and available for navigate up
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click())

        onView(withId(R.id.rv_reviews))
                .check(doesNotExist())
    }
}
