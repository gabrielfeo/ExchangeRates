package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer

import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto.RatesDto
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import java.time.LocalDate

internal interface CurrencyLayerApi {

    suspend fun getLiveRates(
        fixedCurrency: CurrencyUnit, rates: Collection<CurrencyUnit>,
        onResponse: (RatesDto) -> Unit
    )

    suspend fun getHistoricalRates(
        date: LocalDate, fixedCurrency: CurrencyUnit, rates: Collection<CurrencyUnit>,
        onResponse: (RatesDto) -> Unit
    )

}