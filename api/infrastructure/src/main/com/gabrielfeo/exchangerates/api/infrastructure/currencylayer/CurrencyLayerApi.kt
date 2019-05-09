package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import java.time.LocalDate

internal interface CurrencyLayerApi {

    suspend fun getLiveRates(
        fixedCurrency: CurrencyUnit,
        vairableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate>

    suspend fun getHistoricalRates(
        date: LocalDate,
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate>

}