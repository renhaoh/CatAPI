package com.renhao.cats.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cat(
    val id: String,
    @Json(name = "url") val imageUrl: String,
    val width: Int,
    val height: Int
    )