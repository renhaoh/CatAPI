package com.renhao.cats.views

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import com.renhao.cats.R
import com.renhao.cats.utils.TestingUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
// TODO: Consider using Idling Resource instead of BaristaSleep?
class MainActivityTest {

    @Before
    fun before() {
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mActivityTestRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun validateViewContainer() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))
    }

    @Test
    fun validateRecyclerView_loaded() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))

        // TODO: is there a race condition here?
        TestingUtils.waitForId(R.id.random_cat_list_recycler_view, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.error_message)
        BaristaVisibilityAssertions.assertDisplayed(R.id.random_cat_list_recycler_view)

        val recyclerView = onView(withId(R.id.random_cat_list_recycler_view))

        recyclerView.check(TestingUtils.RecyclerViewSizeAssertion(5))
        for (i in 0 until 5) {
            recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i))
                .check(matches(TestingUtils.withViewAtPosition(i, hasDescendant(allOf(withId(R.id.random_cat_card_image), isDisplayed())))))
        }
    }

    @Test
    fun validateRecyclerView_networkError() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")
        // Make sure the internet connectivity is lost
        BaristaSleepInteractions.sleep(2000)

        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))

        TestingUtils.waitForId(R.id.error_message, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.random_cat_list_recycler_view)
        BaristaVisibilityAssertions.assertDisplayed(R.id.error_message)
        onView(withId(R.id.error_message)).check(matches(withText(R.string.random_cat_load_error_network)))
    }

    @Test
    fun validateSwipeToRefresh_loaded() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))
    }

    @Test
    fun validateSwipeToRefresh_networkError() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))
    }

    @After
    fun after() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
        // Make sure WIFI / Internet is turned on
        BaristaSleepInteractions.sleep(5000)
    }

}