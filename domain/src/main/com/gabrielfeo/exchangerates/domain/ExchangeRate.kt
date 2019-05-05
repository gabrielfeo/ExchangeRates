package com.gabrielfeo.exchangerates.domain

import java.math.BigDecimal

/**
 * The exchange rate of [fixedCurrency] to another currency.
 *
 * @property fixedCurrency the base `CurrencyUnit` for this exchange rate.
 */
data class ExchangeRate(
    val fixedCurrency: CurrencyUnit,
    val variableCurrency: CurrencyUnit,
    val value: BigDecimal
) : Comparable<ExchangeRate> {

    override fun compareTo(other: ExchangeRate): Int {
        return if (this.fixedCurrency == other.fixedCurrency) this.value.compareTo(other.value)
        else throw IllegalArgumentException("Two exchange rates with different fixed currencies can't be compared.")
    }

}