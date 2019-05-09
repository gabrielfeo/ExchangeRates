package com.gabrielfeo.exchangerates.domain.currency

import com.gabrielfeo.exchangerates.domain.UnitOfAccount

/**
 * Representation of a ISO 4217 currency unit of account.
 *
 * @property symbol The graphic symbol of this currency, specified in ISO 4217, e.g. US$, €.
 * @property code The unique three-letter designator of this currency, specified in ISO 4217, e.g. USD, EUR
 * @property numericCode The unique three-digit number assigned to this currency by ISO 4217, e.g. 840 for USD, 978 for
 * EUR.
 */
data class CurrencyUnit(
    val symbol: String,
    val code: String,
    val numericCode: Int
) : UnitOfAccount {

    override fun equals(other: Any?): Boolean {
        return if (other is CurrencyUnit) this.numericCode == other.numericCode
        else false
    }

    override fun hashCode(): Int {
        return this.numericCode.hashCode()
    }

}