package com.renhao.cats.viewmodels

import com.renhao.cats.repositories.CatsRepository
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class MainViewModelTest {

    private lateinit var catsRepo: CatsRepository

    @BeforeClass
    fun setup() {
        catsRepo = mockk()
    }

    @Test
    fun fetchRandomCatList_Success() {
        val viewModel = MainViewModel(catsRepo)

    }

    @Test
    fun fetchRandomCatList_Failure() {
        val viewModel = MainViewModel(catsRepo)

    }

    @Test
    fun fetchRandomCatList_NoData() {
        val viewModel = MainViewModel(catsRepo)

    }

    @Test
    fun fetchRandomCatList_Exception() {
        val viewModel = MainViewModel(catsRepo)

    }


}