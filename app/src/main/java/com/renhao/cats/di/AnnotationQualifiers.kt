package com.renhao.cats.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatsRESTService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatsOkHttpClient
