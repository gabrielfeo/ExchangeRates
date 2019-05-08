package com.gabrielfeo.exchangerates.app.infrastructure

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitFactory {

    fun createRetrofit(baseUrl: String, logging: Boolean): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient(logging))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(createJacksonConverterFactory())
            .build()
    }


    private fun createOkHttpClient(logging: Boolean): OkHttpClient {
        return OkHttpClient.Builder()
            .apply { if (logging) addInterceptor(HttpLoggingInterceptor()) }
            .build()
    }

    private fun createJacksonConverterFactory(): JacksonConverterFactory {
        val objectMapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
        return JacksonConverterFactory.create(objectMapper)
    }

}