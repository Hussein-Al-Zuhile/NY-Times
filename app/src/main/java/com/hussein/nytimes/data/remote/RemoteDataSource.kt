package com.hussein.nytimes.data.remote

import com.hussein.nytimes.utility.BASE_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    moshi: Moshi,
    headerInterceptor: HeaderInterceptor,
) {

    val remoteService: RemoteService

    init {
        val okHttpClient = OkHttpClient.Builder().run {
            addInterceptor(headerInterceptor)
            // TODO: Retry
//            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                addInterceptor(loggingInterceptor)
//            }

            build()
        }

        val retrofit = Retrofit.Builder().run {
            baseUrl(BASE_URL)
            client(okHttpClient)
            addConverterFactory(MoshiConverterFactory.create(moshi)).build()
        }

        remoteService = retrofit.create(RemoteService::class.java)
    }

}