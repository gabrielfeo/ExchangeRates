package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.http

import okhttp3.Interceptor
import okhttp3.Response


class QueryParameterInjector(private val name: String, private val value: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val originalUrl = oldRequest.url()
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(name, value)
            .build()
        val newRequest = oldRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }

}