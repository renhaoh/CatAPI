package com.renhao.cats.network

import okhttp3.Response
import retrofit2.http.GET

interface CatsService {

    @GET("v1/images/search")
    suspend fun getCatsList(): Response

}