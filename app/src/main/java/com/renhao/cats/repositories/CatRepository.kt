package com.renhao.cats.repositories

import com.renhao.cats.BuildConfig
import com.renhao.cats.network.CatsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CatRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    companion object {
        const val CATS_URL = "https://api.thecatapi.com"
    }

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

    suspend fun fetchRandomCat() {
        val x = withContext(ioDispatcher) {
            catsServices.getRandomCat()
        }
        val j = 10
    }
}