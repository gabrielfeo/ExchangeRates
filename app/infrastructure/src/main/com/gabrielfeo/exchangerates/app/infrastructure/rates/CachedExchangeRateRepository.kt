@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.infrastructure.rates

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import retrofit2.Retrofit
import retrofit2.create
import java.time.OffsetDateTime

class CachedExchangeRateRepository(
    retrofit: Retrofit
) : ExchangeRateRepository {

    private val remoteService: ExchangeRatesService = retrofit.create<ExchangeRatesService>()
    // TODO Cache

    override suspend fun getRate(
        fixedCurrency: CurrencyUnit,
        variableCurrency: CurrencyUnit
    ): ExchangeRate {
        return remoteService.getCurrentRates(fixedCurrency.code, listOf(variableCurrency.code))
            .await()
            .first()
    }

    override suspend fun getRates(
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        return remoteService.getCurrentRates(fixedCurrency.code, variableCurrencies.map { currency -> currency.code })
            .await()
    }

    override suspend fun getRatesAt(
        time: OffsetDateTime,
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate> {
        return remoteService.getPastRates(
            time.toString(),
            fixedCurrency.code,
            variableCurrencies.map { currency -> currency.code }
        ).await()
    }

}