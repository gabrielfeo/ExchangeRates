package com.gabrielfeo.exchangerates.domain.tests

import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * This test suite asserts that ExchangeRate complies to expected business rules.
 */
class ExchangeRateTest {

    private val usdToEurRate = ExchangeRate(usDollar, euro, BigDecimal.valueOf(0.89))
    private val usdToBrlRate = ExchangeRate(usDollar, brazilianReal, BigDecimal.valueOf(3.94))
    private val brlToUsdRate = ExchangeRate(brazilianReal, usDollar, BigDecimal.valueOf(0.25))
    private val brlToEurRate = ExchangeRate(brazilianReal, euro, BigDecimal.valueOf(0.25))

    @Test
    fun `Rate is not comparable between different base currencies`() {
        assertThrows(IllegalArgumentException::class.java) {
            usdToBrlRate.compareTo(brlToUsdRate)
        }
    }

    @Test
    fun `Rate is comparable by value`() {
        assertTrue(usdToBrlRate > usdToEurRate)
    }

    @Test
    fun `Rates are equatable by value`() {
        assertTrue(brlToUsdRate == brlToEurRate)
    }

}