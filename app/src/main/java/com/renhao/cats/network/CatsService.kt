package com.renhao.cats.network

import com.renhao.cats.models.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsService {

    @GET("v1/images/search")
    suspend fun getRandomCat(): Response<List<Cat>>

    @GET("v1/images/search?order=rand")
    suspend fun getRandomCatList(@Query("limit") limit: Int, @Query("page") page: Int): Response<List<Cat>>

}