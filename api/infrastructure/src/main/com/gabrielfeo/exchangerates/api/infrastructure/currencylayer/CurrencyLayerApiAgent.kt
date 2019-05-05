package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer

import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.RatesDto
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.parametersOf
import java.time.LocalDate

internal class CurrencyLayerApiAgent : CurrencyLayerApi {

    private val client by lazy { io.ktor.client.HttpClient { install(JsonFeature) } }

    internal companion object {
        const val baseApiUrl = "https://apilayer.net/api"
        const val liveRatesUrl = "$baseApiUrl/live"
        const val historicalRatesUrl = "$baseApiUrl/historical"
    }

    override suspend fun getLiveRates(
        fixedCurrency: CurrencyUnit,
        rates: Collection<CurrencyUnit>,
        onResponse: (RatesDto) -> Unit
    ) {
        val ratesDto = client.request<RatesDto>(liveRatesUrl) {
            parameter("source", fixedCurrency.code)
            parametersOf("currencies", rates.map { rate -> rate.code })
        }
        onResponse(ratesDto)
    }

    override suspend fun getHistoricalRates(
        date: LocalDate,
        fixedCurrency: CurrencyUnit,
        rates: Collection<CurrencyUnit>,
        onResponse: (RatesDto) -> Unit
    ) {
        val ratesDto = client.request<RatesDto>(historicalRatesUrl) {
            parameter("date", date.toString())
            parameter("source", fixedCurrency.code)
            parametersOf("currencies", rates.map { rate -> rate.code })
        }
        onResponse(ratesDto)
    }

}