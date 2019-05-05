package com.gabrielfeo.exchangerates.domain.currency

/**
 * Contains all currently circulating currencies in the world.
 */
interface CurrencyUnitRepository {

    /**
     * Currently circulating currencies.
     */
    val currencies: Collection<CurrencyUnit>

}