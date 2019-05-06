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
    suspend fun getRate(fixedCurrency: CurrencyUnit, variableCurrency: CurrencyUnit): ExchangeRate

    /**
     * Gets the current value of 1.00 [fixedCurrency] in each of the given `CurrencyUnit`s, [variableCurrencies].
     *
     * @param fixedCurrency the base `CurrencyUnit`
     * @param variableCurrencies the `CurrencyUnit`s [fixedCurrency] should be converted to
     * @return `ExchangeRate`s of 1.00 [fixedCurrency] converted to each of the [variableCurrencies]
     */
    suspend fun getRates(
        fixedCurrency: CurrencyUnit,
        variableCurrencies: Collection<CurrencyUnit>
    ): Collection<ExchangeRate>

}