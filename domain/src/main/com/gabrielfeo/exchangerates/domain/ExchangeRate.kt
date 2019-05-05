package com.gabrielfeo.exchangerates.domain

import java.math.BigDecimal

/**
 * The exchange rate of [fixedCurrency] to another currency.
 *
 * @property fixedCurrency the base `CurrencyUnit` for this exchange rate.
 */
data class ExchangeRate(val fixedCurrency: CurrencyUnit) {

    /**
     * Converts the value of 1.00 [fixedCurrency] to another `CurrencyUnit`.
     * @param other the `CurrencyUnit` that [fixedCurrency] should be converted to
     * @return the equivalent value of 1.00 [fixedCurrency] in [other]
     */
    fun toCurrency(other: CurrencyUnit): BigDecimal {
        TODO()
    }

}