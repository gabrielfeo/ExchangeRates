package com.gabrielfeo.exchangerates.domain.currency.rate

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
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

    override fun equals(other: Any?): Boolean {
        return if (other is ExchangeRate) this.value == other.value
        else false
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

}