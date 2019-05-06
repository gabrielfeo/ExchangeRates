@file:Suppress("NOTHING_TO_INLINE")

package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer

import com.fasterxml.jackson.databind.DeserializationFeature
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.HistoricalRatesDto
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.LiveRatesDto
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception.ApiException
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.http.QueryParameterInjector
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import io.ktor.client.HttpClient
import io.ktor.client.call.typeInfo
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.response.HttpResponse
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class CurrencyLayerApiAgent(
    private val apiKey: String,
    private val currencyRepository: CurrencyUnitRepository
) : CurrencyLayerApi {

    private val serializer: JsonSerializer by lazy {
        JacksonSerializer {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    private val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = this@CurrencyLayerApiAgent.serializer
            }
            engine {
                addInterceptor(QueryParameterInjector("access_key", apiKey))
            }
        }
    }

    internal companion object {
        const val baseApiUrl = "http://apilayer.net/api"
        const val liveRatesUrl = "$baseApiUrl/live"
        const val historicalRatesUrl = "$baseApiUrl/historical"
    }


    override suspend fun getLiveRates(
        fixedCurrency: CurrencyUnit,
        vairableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        try {
            val dto = client.request<LiveRatesDto>(liveRatesUrl) {
                parameter("source", fixedCurrency.code)
                parameter("currencies", vairableCurrencies.map { rate -> rate.code }.joinToString(","))
            }
            return dto.mappedToDomainModel()
        } catch (error: Throwable) {
            throw ApiException("Failed to get live vairableCurrencies for ${fixedCurrency.code}.", cause = error)
        }
    }

    override suspend fun getHistoricalRates(
        date: LocalDate,
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        try {
            val dto = client.request<HistoricalRatesDto>(historicalRatesUrl) {
                parameter("date", date.toString())
                parameter("source", fixedCurrency.code)
                parameter("currencies", variableCurrencies.joinToString(",") { rate -> rate.code })
            }
            return dto.mappedToDomainModel()
        } catch (error: Throwable) {
            throw ApiException("Failed to get live variableCurrencies for ${fixedCurrency.code}.", cause = error)
        }
    }


    private fun LiveRatesDto.mappedToDomainModel(): Collection<ExchangeRate> {
        return quotes.map { (currenciesString, value) ->
            val time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("UTC"))
            parseCurrencies(currenciesString)?.let { (fixedCurrency, variableCurrency) ->
                ExchangeRate(fixedCurrency, variableCurrency, value.toBigDecimal(), time)
            }
        }.filterNotNull()
    }

    private fun HistoricalRatesDto.mappedToDomainModel(): Collection<ExchangeRate> {
        return quotes.map { (currenciesString, value) ->
            val time = LocalDate.parse(date, DateTimeFormatter.ISO_DATE).atTime(0, 0)
            parseCurrencies(currenciesString)?.let { (fixedCurrency, variableCurrency) ->
                ExchangeRate(fixedCurrency, variableCurrency, value.toBigDecimal(), time)
            }
        }.filterNotNull()
    }

    private fun parseCurrencies(codePair: String): Pair<CurrencyUnit, CurrencyUnit>? {
        val fixedCurrency = currencyRepository[codePair.substring(0..2)]
        val variableCurrency = currencyRepository[codePair.substring(3..5)]
        return if (fixedCurrency != null && variableCurrency != null) Pair(fixedCurrency, variableCurrency)
        else null
    }


    private suspend inline fun <reified T> JsonSerializer.readAs(response: HttpResponse): T? =
        runCatching { read(typeInfo<T>(), response) as T }.getOrNull()

}