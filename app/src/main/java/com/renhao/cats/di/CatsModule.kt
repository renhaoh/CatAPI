package com.renhao.cats.di

import com.renhao.cats.BuildConfig
import com.renhao.cats.network.CatsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class CatsModule {

    companion object {
        const val CATS_URL = "https://api.thecatapi.com"
    }

    @CatsOkHttpClient
    @Provides
    fun providesCatsOkHttpClient(): OkHttpClient {
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
            return builder.build()
    }

    @CatsRESTService
    @Provides
    fun providesCatsService(
        @CatsOkHttpClient catsOkHttpClient: OkHttpClient
    ): CatsService {
        return Retrofit.Builder()
            .client(catsOkHttpClient)
            .baseUrl(CATS_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(CatsService::class.java)
    }

}