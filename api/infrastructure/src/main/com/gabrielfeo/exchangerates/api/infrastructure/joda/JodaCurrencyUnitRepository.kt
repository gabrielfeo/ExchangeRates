package com.gabrielfeo.exchangerates.api.infrastructure.joda

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository

class JodaCurrencyUnitRepository : CurrencyUnitRepository {

    // TODO Check whether this will have any "obsolete" currencies
    override val currencies: Collection<CurrencyUnit> by lazy {
        org.joda.money.CurrencyUnit.registeredCurrencies()
            .filter { !it.isPseudoCurrency }
            .map { jodaCurrencyUnit ->
                CurrencyUnit(jodaCurrencyUnit.symbol, jodaCurrencyUnit.code, jodaCurrencyUnit.numericCode)
            }
    }

}