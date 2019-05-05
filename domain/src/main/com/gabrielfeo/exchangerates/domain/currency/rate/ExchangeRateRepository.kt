package com.gabrielfeo.exchangerates.domain.currency.rate

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit

/**
 * A repository where current exchange rates can be retrieved
 */
interface ExchangeRateRepository {

    /**
     * Gets the current value of 1.00 [fixedCurrency] in another `CurrencyUnit`, [variableCurrency].
     *
     * @param fixedCurrency the base `CurrencyUnit`
     * @param variableCurrency the `CurrencyUnit` of the resulting `ExchangeRate` value
     * @return the `ExchangeRate` of 1.00 [fixedCurrency] in [variableCurrency]
     */
    fun getRate(fixedCurrency: CurrencyUnit, variableCurrency: CurrencyUnit): ExchangeRate

}