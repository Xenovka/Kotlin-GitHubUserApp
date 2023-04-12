package com.dicoding.githubuser.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.dicoding.githubuser.R

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyInput = "wendy"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertSearchInputTest() {
        // Check if the input field is empty
        onView(withId(R.id.et_user_name)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.iv_search_icon)).perform(click())
        onView(withId(R.id.et_user_name)).check(matches(hasErrorText("This field can not be empty")))

        // Check if there's an input
        onView(withId(R.id.et_user_name)).perform(typeText(dummyInput), closeSoftKeyboard())
        onView(withId(R.id.iv_search_icon)).perform(click())

    }
}