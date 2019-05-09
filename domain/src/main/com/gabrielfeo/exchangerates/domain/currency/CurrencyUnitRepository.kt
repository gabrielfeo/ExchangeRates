package com.gabrielfeo.exchangerates.domain.currency

/**
 * Contains all currently circulating currencies in the world.
 */
interface CurrencyUnitRepository {

    /**
     * Currently circulating currencies.
     */
    val currencies: Collection<CurrencyUnit>

    /**
     * Gets a currency by [CurrencyUnit.code].
     * @return the `CurrencyUnit` of the given [currencyCode], or null if it's not a circulating currency.
     */
    operator fun get(currencyCode: String): CurrencyUnit?

}