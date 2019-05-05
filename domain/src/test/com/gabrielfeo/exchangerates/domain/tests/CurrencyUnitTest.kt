package com.gabrielfeo.exchangerates.domain.tests

import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

/**
 * This test suite asserts that certain business rules and domain concepts are properly implemented.
 */
class CurrencyUnitTest {

    private val usDollar: CurrencyUnit = CurrencyUnit("United States Dollar", "US$", "USD", 840)

    @Test
    fun `Currency is identified by ISO numeric code`() {
        val fakeUsDollar = CurrencyUnit(usDollar.fullName, usDollar.symbol, usDollar.code, usDollar.numericCode + 1)
        assertFalse(fakeUsDollar == usDollar)
    }

}