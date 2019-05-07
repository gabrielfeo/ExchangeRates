package com.gabrielfeo.exchangerates.app.infrastructure.currency

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository

class JodaCurrencyUnitRepository : CurrencyUnitRepository {

    override val currencies: Collection<CurrencyUnit> by lazy {
        org.joda.money.CurrencyUnit.registeredCurrencies()
            .filter { !it.isPseudoCurrency }
            .map { jodaCurrencyUnit ->
                CurrencyUnit(jodaCurrencyUnit.symbol, jodaCurrencyUnit.code, jodaCurrencyUnit.numericCode)
            }
    }

    override fun get(currencyCode: String): CurrencyUnit? {
        return currencies.firstOrNull { it.code == currencyCode }
    }

}