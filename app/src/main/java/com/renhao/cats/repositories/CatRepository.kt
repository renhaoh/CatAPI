package com.renhao.cats.repositories

import com.renhao.cats.network.CatsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CatRepository {
    companion object {
        const val CATS_URL = "https://api.thecatapi.com"
    }

    private val catsHttpClient by lazy {
        val client = OkHttpClient()
        client.interceptors().add(
            Interceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("x-api-key", "MY_API_KEY") // <-- this is the important line

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        )
        client
    }

    private val catsRetrofit by lazy {
        Retrofit.Builder()
            .client(catsHttpClient)
            .baseUrl(CATS_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val catsServices by lazy {
        catsRetrofit.create(CatsService::class.java)
    }
}