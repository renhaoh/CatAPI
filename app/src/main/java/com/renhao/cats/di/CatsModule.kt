package com.renhao.cats.di

import android.content.Context
import com.renhao.cats.BuildConfig
import com.renhao.cats.network.CatsService
import com.renhao.cats.utils.CacheUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class CatsModule {

    companion object {
        const val CATS_URL = "https://api.thecatapi.com"
    }

    @CatsOkHttpClient
    @Provides
    fun providesCatsOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val cacheMaxAge = 3
        val cacheSize = (cacheMaxAge * CacheUtils.ONE_MB).toLong()
        val cache = Cache(context.cacheDir, cacheSize)

        val builder = OkHttpClient.Builder()
        builder
            .cache(cache)
            .addInterceptor(
            Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                if (CacheUtils.hasNetwork(context)) {
                    requestBuilder.addHeader("Cache-Control", "public, max-age=$cacheMaxAge" )
                } else {
                    requestBuilder.addHeader("Cache-Control", "public, only-if-cached")
                }
                
                requestBuilder.addHeader("x-api-key", BuildConfig.CATS_API_KEY)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        )
            .callTimeout(5000L, TimeUnit.MILLISECONDS)
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