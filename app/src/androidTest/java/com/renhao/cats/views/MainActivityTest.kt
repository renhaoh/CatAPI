package com.renhao.cats.views

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
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
// TODO: Figure out Flakiness - why do they work through debug but not run?
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
        disableInternet()

        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))

        TestingUtils.waitForId(R.id.error_message, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.random_cat_list_recycler_view)
        BaristaVisibilityAssertions.assertDisplayed(R.id.error_message)
        onView(withId(R.id.error_message)).check(matches(withText(R.string.random_cat_load_error_network)))
    }

    @Test
    fun validateSwipeToRefresh_loaded() {
        disableInternet()

        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))

        TestingUtils.waitForId(R.id.error_message, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.random_cat_list_recycler_view)
        BaristaVisibilityAssertions.assertDisplayed(R.id.error_message)

        enableInternet()

        performSwipeRefreshAction()

        TestingUtils.waitForId(R.id.random_cat_list_recycler_view, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.error_message)
        BaristaVisibilityAssertions.assertDisplayed(R.id.random_cat_list_recycler_view)

        val recyclerView = onView(withId(R.id.random_cat_list_recycler_view))

        recyclerView.check(TestingUtils.RecyclerViewSizeAssertion(5))

    }

    @Test
    fun validateSwipeToRefresh_networkError() {
        onView(withId(R.id.cat_list_fragment_container)).check(matches(isDisplayed()))

        TestingUtils.waitForId(R.id.random_cat_list_recycler_view, 5000)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.error_message)
        BaristaVisibilityAssertions.assertDisplayed(R.id.random_cat_list_recycler_view)

        disableInternet()

        performSwipeRefreshAction()

        BaristaVisibilityAssertions.assertDisplayed(R.id.error_message)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.random_cat_list_recycler_view)

    }

    @After
    fun after() {
        enableInternet()
    }

    private fun performSwipeRefreshAction() {
        onView(withId(R.id.random_cat_list_swipe_refresh))
            .perform(TestingUtils.withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
        BaristaSleepInteractions.sleep(5000)
    }

    private fun enableInternet() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
        // Leave enough time for wifi / data to connect
        BaristaSleepInteractions.sleep(10000)
    }

    private fun disableInternet() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")
        // Make sure the internet connectivity is lost
        BaristaSleepInteractions.sleep(5000)
    }

}