package com.renhao.cats.network

import com.renhao.cats.models.Cat
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("v1/images/search")
    suspend fun getRandomCat(): Response<List<Cat>>

}