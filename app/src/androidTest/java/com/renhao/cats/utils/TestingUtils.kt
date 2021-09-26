package com.renhao.cats.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import androidx.test.espresso.ViewAction

object TestingUtils {

    private fun searchFor(matcher: Matcher<View>): ViewAction {

        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "searching for view $matcher in the root view"
            }

            override fun perform(uiController: UiController, view: View) {

                var tries = 0
                val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)
                childViews.forEach {
                    tries++
                    if (matcher.matches(it)) {
                        return
                    }
                }

                throw NoMatchingViewException.Builder()
                    .withRootView(view)
                    .withViewMatcher(matcher)
                    .build()
            }
        }
    }

    // from: https://stackoverflow.com/questions/49796132/android-espresso-wait-for-text-to-appear/49814995
    fun waitForId(@IdRes viewId: Int, timeoutMillis: Long) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeoutMillis

        var error: Exception? = null

        while (System.currentTimeMillis() < endTime) {
            try {
                onView(isRoot()).perform(searchFor(withId(viewId)))
                return
            } catch(e: Exception) {
                BaristaSleepInteractions.sleep(500)
                error = e
            }
        }
        throw PerformException.Builder().withCause(error).withViewDescription(viewId.toString()).build()
    }

    // from: https://stackoverflow.com/questions/39688297/how-to-check-if-a-view-is-visible-on-a-specific-recyclerview-item
    fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View?> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    // from: https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
    class RecyclerViewSizeAssertion(private val checkItemCount: Int): ViewAssertion {
        override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
            view?.let {
                val recycler = view as? RecyclerView
                assertThat(recycler?.adapter?.itemCount, `is`(checkItemCount))
            } ?: run { noViewFoundException?.let { exception -> throw exception } }
        }
    }

    // https://stackoverflow.com/questions/33505953/espresso-how-to-test-swiperefreshlayout
    fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }

}