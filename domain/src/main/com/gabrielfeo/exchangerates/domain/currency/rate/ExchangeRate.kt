package com.gabrielfeo.exchangerates.domain.currency.rate

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * The exchange rate of [fixedCurrency] to another currency.
 *
 * @property fixedCurrency The base `CurrencyUnit` for this exchange rate.
 * @property variableCurrency The `CurrencyUnit` against which [fixedCurrency] is evaluated.
 * @property value The exchange rate value. 1.00 [fixedCurrency] converted to [variableCurrency].
 * @property time The date and time of this valuation.
 */
data class ExchangeRate(
    val fixedCurrency: CurrencyUnit,
    val variableCurrency: CurrencyUnit,
    val value: BigDecimal,
    val time: LocalDateTime
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