package com.gabrielfeo.exchangerates.api.infrastructure

import com.gabrielfeo.exchangerates.api.infrastructure.dto.ErrorDto
import com.gabrielfeo.exchangerates.api.infrastructure.dto.LiveRatesDto
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import java.time.LocalDate

internal interface CurrencyLayerApi {

    fun getLiveRates(
        fixedCurrency: CurrencyUnit, rates: Collection<CurrencyUnit>,
        onSuccess: (LiveRatesDto) -> Unit, onError: (ErrorDto) -> Unit
    )

    fun getHistoricalRates(
        date: LocalDate, fixedCurrency: CurrencyUnit, rates: Collection<CurrencyUnit>,
        onSuccess: (LiveRatesDto) -> Unit, onError: (ErrorDto) -> Unit
    )

}