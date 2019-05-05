package com.gabrielfeo.exchangerates.domain

import java.math.BigDecimal

/**
 * Representation of a currency unit of account.
 *
 * @property fullName The complete name of this currency, e.g. "United States Dollar", "Euro".
 * @property symbol The graphic symbol of this currency, specified in ISO 4217, e.g. US$, €.
 * @property code The unique three-letter designator of this currency, specified in ISO 4217, e.g. USD, EUR
 * @property numericCode The unique three-digit number assigned to this currency by ISO 4217, e.g. 840 for USD, 978 for
 * EUR.
 */
data class CurrencyUnit(
    val fullName: String,
    val symbol: String,
    val code: String,
    val numericCode: Int
) : UnitOfAccount {

    val exchangeRate = ExchangeRate(this)

    override fun valueIn(other: UnitOfAccount): BigDecimal {
        if (other is CurrencyUnit) return this.exchangeRate.toCurrency(other)
        else throw UnsupportedOperationException("Converting a currency to a non-currency unit of account is not supported.")
    }

    override fun compareTo(other: UnitOfAccount): Int {
        return this.valueIn(other).compareTo(BigDecimal.ZERO)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is CurrencyUnit) this.numericCode == other.numericCode
        else false
    }

    override fun hashCode(): Int {
        return this.numericCode.hashCode()
    }

}