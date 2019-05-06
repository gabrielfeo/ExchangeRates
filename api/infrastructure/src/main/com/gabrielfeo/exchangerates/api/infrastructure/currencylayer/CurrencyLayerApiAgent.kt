@file:Suppress("NOTHING_TO_INLINE")

package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer

import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.ErrorDto
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.HistoricalRatesDto
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.LiveRatesDto
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception.ApiException
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.typeInfo
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.request.parameter
import io.ktor.client.response.HttpResponse
import io.ktor.http.parametersOf
import java.time.LocalDate

internal class CurrencyLayerApiAgent : CurrencyLayerApi {

    private val serializer: JsonSerializer by lazy { JacksonSerializer() }
    private val client: HttpClient by lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = this@CurrencyLayerApiAgent.serializer
            }
        }
    }

    internal companion object {
        const val baseApiUrl = "https://apilayer.net/api"
        const val liveRatesUrl = "$baseApiUrl/live"
        const val historicalRatesUrl = "$baseApiUrl/historical"
    }

    override suspend fun getLiveRates(
        fixedCurrency: CurrencyUnit,
        rates: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        runCatching {
            client.call(liveRatesUrl) {
                parameter("source", fixedCurrency.code)
                parametersOf("currencies", rates.map { rate -> rate.code })
            }
        }.onSuccess { successfulCall ->
            serializer.readAs<LiveRatesDto>(successfulCall.response)?.let { return it.mappedToDomainModel() }
            serializer.readAs<ErrorDto>(successfulCall.response)?.let { throw it.toException() }
        }
        throw ApiException.unknown
    }

    override suspend fun getHistoricalRates(
        date: LocalDate,
        fixedCurrency: CurrencyUnit,
        rates: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        runCatching {
            client.call(historicalRatesUrl) {
                parameter("date", date.toString())
                parameter("source", fixedCurrency.code)
                parametersOf("currencies", rates.map { rate -> rate.code })
            }
        }.onSuccess { successfulCall ->
            serializer.readAs<HistoricalRatesDto>(successfulCall.response)?.let { return it.mappedToDomainModel() }
            serializer.readAs<ErrorDto>(successfulCall.response)?.let { throw it.toException() }
        }
        throw ApiException.unknown
    }

    private fun LiveRatesDto.mappedToDomainModel(): Collection<ExchangeRate> {
        TODO()
    }

    private fun HistoricalRatesDto.mappedToDomainModel(): Collection<ExchangeRate> {
        TODO()
    }

    private suspend inline fun <reified T> JsonSerializer.readAs(response: HttpResponse): T? =
        runCatching { read(typeInfo<T>(), response) as T }.getOrNull()

}