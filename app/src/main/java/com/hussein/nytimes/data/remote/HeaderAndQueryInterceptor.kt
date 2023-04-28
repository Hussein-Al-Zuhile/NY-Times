package com.hussein.nytimes.data.remote

import com.hussein.nytimes.utility.API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderAndQueryInterceptor @Inject constructor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithApiKey =
            chain.run {
                val urlWithAPiKey = request().url.newBuilder()
                    .addQueryParameter("api-key", API_KEY)
                    .build()

                request().newBuilder().url(urlWithAPiKey).build()
            }
        return chain.proceed(requestWithApiKey)
    }
}