package com.renhao.cats.views

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.renhao.cats.R
import com.renhao.cats.utils.launchFragmentInHiltContainer
import com.renhao.cats.views.fragments.CatsListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// NOTE: https://developer.android.com/guide/fragments/test#declare-dependencies - there is a build issue
// that stems from this that isn't fixed / solution doesn't work for this build
// TODO: update this when ready
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CatsListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun validateRecyclerView() {
        val scenario = launchFragmentInHiltContainer<CatsListFragment>()
//        Espresso.onView(ViewMatchers.withId(R.id.random_cat_list_recycler_view))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @After
    fun after() { }
}