package com.anhhoang.popularmovies

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import android.widget.TextView

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.StringStartsWith.startsWith

/**
 * Created by anh.hoang on 6/1/17.
 */

@RunWith(AndroidJUnit4::class)
class MoviesRecyclerViewTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(MoviesActivity::class.java)

    @Test
    fun onItemsClick_activityChanges() {
        onView(withId(R.id.rv_movies))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // On click success next screen should have title
        onView(
                allOf<View>(
                        instanceOf<Any>(TextView::class.java),
                        withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(startsWith(""))))
    }
}
