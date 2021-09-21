package com.renhao.cats.repositories

import com.renhao.cats.BuildConfig
import com.renhao.cats.di.CatsRESTService
import com.renhao.cats.models.Cat
import com.renhao.cats.network.CatsService
import com.renhao.cats.network.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class CatRepository @Inject constructor(
    @CatsRESTService private val catsService: CatsService
) {

    // TODO: inject this later
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun fetchRandomCat(): NetworkResponse<List<Cat>> {
       return withContext(ioDispatcher) {
            NetworkResponse.create(catsService.getRandomCat())
        }
    }

    suspend fun fetchRandomCatList(limit: Int = 5, page: Int = 0): NetworkResponse<List<Cat>> {
        return withContext(ioDispatcher) {
            NetworkResponse.create(catsService.getRandomCatList(limit, page))
        }
    }
}