@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.infrastructure.rates

import com.gabrielfeo.exchangerates.app.infrastructure.rates.dto.ExchangeRatesResponse
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import retrofit2.Retrofit
import retrofit2.create
import java.time.LocalDateTime
import java.time.OffsetDateTime

class CachedExchangeRateRepository(
    private val currencyUnitRepository: CurrencyUnitRepository,
    retrofit: Retrofit
) : ExchangeRateRepository {

    private val remoteService: ExchangeRatesService = retrofit.create<ExchangeRatesService>()
    // TODO Cache

    override suspend fun getRate(
        fixedCurrency: CurrencyUnit,
        variableCurrency: CurrencyUnit
    ): ExchangeRate {
        val response = remoteService.getCurrentRates(fixedCurrency.code, listOf(variableCurrency.code)).await()
        return response.mappedToDomainModel().first()
    }

    override suspend fun getRates(
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        val variableCurrencyCodes = variableCurrencies.map { currency -> currency.code }
        val response = remoteService.getCurrentRates(fixedCurrency.code, variableCurrencyCodes).await()
        return response.mappedToDomainModel()
    }

    override suspend fun getRatesAt(
        time: OffsetDateTime,
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        val variableCurrencyCodes = variableCurrencies.map { currency -> currency.code }
        val response = remoteService.getPastRates(time.toString(), fixedCurrency.code, variableCurrencyCodes).await()
        return response.mappedToDomainModel()
    }

    private fun ExchangeRatesResponse.mappedToDomainModel(): Collection<ExchangeRate> {
        val fixedCurrency = currencyUnitRepository[this.currency]
        val dateTime = LocalDateTime.parse(this.time)
        return exchangeRatesMap
            .map { (variableCurrency, value) ->
                ExchangeRate(
                    fixedCurrency!!, currencyUnitRepository[variableCurrency]!!, // Should throw if response isn't valid
                    value.toBigDecimal(), dateTime
                )
            }
    }

}