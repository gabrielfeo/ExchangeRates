package com.gabrielfeo.exchangerates.api.infrastructure.tests

import com.gabrielfeo.exchangerates.api.infrastructure.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JodaCurrencyUnitRepositoryTest {

    private val currencyUnitRepository: CurrencyUnitRepository = JodaCurrencyUnitRepository()

    @Test
    fun `Repository contains circulating currencies`() {
        assertTrue(
            currencyUnitRepository.currencies.any { currency -> currency.code == "USD" }
        )
    }

    @Test
    fun `Repository does not contain any former currencies`() {
        assertTrue(currencyUnitRepository.currencies.none { currency ->
            currency.code == "BRR" // Brazilian Cruzeiro Real
                    || currency.code == "BOP" // Bolivian Peso
                    || currency.code == "DEM" // German Mark
                    || currency.code == "TRL" // 1st Turkish Lira
        })
    }

}