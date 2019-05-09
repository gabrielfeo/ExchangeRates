@file:Suppress("DeferredIsResult")

package com.gabrielfeo.exchangerates.app.infrastructure.rates

import com.gabrielfeo.exchangerates.app.infrastructure.rates.dto.ExchangeRatesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ExchangeRatesService {

    @GET("api/rates/current")
    fun getCurrentRates(
        @Query("fixed") fixedCurrencyCode: String,
        @Query("variable") variableCurrencyCodes: Collection<String>
    ): Deferred<ExchangeRatesResponse>

    @GET("api/rates/past")
    fun getPastRates(
        @Query("time") time: String,
        @Query("fixed") fixedCurrencyCode: String,
        @Query("variable") variableCurrencyCodes: Collection<String>
    ): Deferred<ExchangeRatesResponse>

}