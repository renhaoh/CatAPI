package com.renhao.cats.repositories

import com.renhao.cats.models.Cat
import com.renhao.cats.network.CatsService
import com.renhao.cats.network.NetworkResponse
import com.renhao.cats.repositories.CatsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Response

class CatsRepositoryTest {

    private lateinit var catsService: CatsService

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        catsService = mockk()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchRandomCatList_Success() {
        coEvery {
            catsService.getRandomCatList(any(), any())
        } returns Response.success(200, listOf(Cat(id = "myId1", imageUrl = "myURL1", width = 250, height = 500)))

        val repo = CatsRepository(catsService, testDispatcher)
        runBlocking {
            val result = repo.fetchRandomCatList()
            Assert.assertTrue(result is NetworkResponse.Success)

            val list = (result as? NetworkResponse.Success<List<Cat>>)?.data
            Assert.assertEquals(1, list?.size)
            Assert.assertEquals("myURL1", list?.firstOrNull()?.imageUrl)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchRandomCatList_Failure() {
        coEvery {
            catsService.getRandomCatList(any(), any())
        } returns Response.error(403, ResponseBody.create(MediaType.get("application/json"), ""))

        val repo = CatsRepository(catsService, testDispatcher)
        runBlocking {
            val result = repo.fetchRandomCatList()
            Assert.assertTrue(result is NetworkResponse.Error)

            (result as? NetworkResponse.Error<List<Cat>>)?.let {
                Assert.assertEquals(403, it.errorCode)
                Assert.assertEquals("Response.error()", it.errorMessage) // From Retrofit/OkHttp
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchRandomCatList_NoData() {
        coEvery {
            catsService.getRandomCatList(any(), any())
        } returns Response.success(200, listOf())

        val repo = CatsRepository(catsService, testDispatcher)
        runBlocking {
            val result = repo.fetchRandomCatList()
            Assert.assertTrue(result is NetworkResponse.Success)

            val list = (result as? NetworkResponse.Success<List<Cat>>)?.data
            Assert.assertEquals(0, list?.size)
        }
    }


}