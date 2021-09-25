package com.renhao.cats.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.renhao.cats.R
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun before() {

    }

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun validateViewContainer() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))
    }

    @After
    fun after() {

    }

}