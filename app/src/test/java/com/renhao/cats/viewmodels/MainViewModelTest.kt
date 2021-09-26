package com.renhao.cats.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.renhao.cats.R
import com.renhao.cats.models.Cat
import com.renhao.cats.network.DataResult
import com.renhao.cats.network.NetworkResponse
import com.renhao.cats.repositories.CatsRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber


class MainViewModelTest {

    private lateinit var catsRepo: CatsRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(Timber)
        catsRepo = mockk()
        viewModel = MainViewModel(catsRepo)
    }

    @Test
    fun fetchRandomCatList_Success() {
        val responseList = listOf(
            Cat(id = "myId1", imageUrl = "myURL1", width = 250, height = 500),
            Cat(id = "myId2", imageUrl = "myURL2", width = 300, height = 600))
        coEvery {
            catsRepo.fetchRandomCatList(any(), any())
        } returns NetworkResponse.Success(responseList)

        viewModel.fetchRandomCatList()

        val result = viewModel.randomCatListLiveData.value
        Assert.assertTrue(result is DataResult.Success)
        Assert.assertEquals(responseList, (result as? DataResult.Success)?.data)
    }

    @Test
    fun fetchRandomCatList_Failure() {
        coEvery {
            catsRepo.fetchRandomCatList(any(), any())
        } returns NetworkResponse.Error(404, "sample error")

        viewModel.fetchRandomCatList()

        verify {
            Timber.e("Network error with code: 404 and message: sample error")
        }

        val result = viewModel.randomCatListLiveData.value
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(R.string.random_cat_load_error_available, (result as? DataResult.Error)?.messageId)
    }

    @Test
    fun fetchRandomCatList_NoData() {
        coEvery {
            catsRepo.fetchRandomCatList(any(), any())
        } returns NetworkResponse.Success(null)

        viewModel.fetchRandomCatList()

        verify {
            Timber.e("Empty data response")
        }

        val result = viewModel.randomCatListLiveData.value
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(R.string.random_cat_load_error_available, (result as? DataResult.Error)?.messageId)
    }

    @Test
    fun fetchRandomCatList_Exception() {
        val exception = Exception("exception message")
        coEvery {
            catsRepo.fetchRandomCatList(any(), any())
        } throws exception

        viewModel.fetchRandomCatList()

        verify {
            Timber.e(exception)
        }

        val result = viewModel.randomCatListLiveData.value
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(R.string.random_cat_load_error_network, (result as? DataResult.Error)?.messageId)
    }

}