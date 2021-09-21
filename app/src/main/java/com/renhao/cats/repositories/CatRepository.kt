package com.renhao.cats.repositories

import com.renhao.cats.BuildConfig
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

class CatRepository @Inject constructor() {
    companion object {
        const val CATS_URL = "https://api.thecatapi.com"
    }

    // TODO: inject this later
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    // TODO: Inject this later?
    private val catsHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(
            Interceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("x-api-key", BuildConfig.CATS_API_KEY)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        )
        builder.build()
    }

    private val catsRetrofit by lazy {
        Retrofit.Builder()
            .client(catsHttpClient)
            .baseUrl(CATS_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catsServices by lazy {
        catsRetrofit.create(CatsService::class.java)
    }

    suspend fun fetchRandomCat(): NetworkResponse<List<Cat>> {
       return withContext(ioDispatcher) {
            NetworkResponse.create(catsServices.getRandomCat())
        }
    }

    suspend fun fetchRandomCatList(limit: Int = 5, page: Int = 0): NetworkResponse<List<Cat>> {
        return withContext(ioDispatcher) {
            NetworkResponse.create(catsServices.getRandomCatList(limit, page))
        }
    }
}