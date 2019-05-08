package com.gabrielfeo.exchangerates.domain.tests

import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This test suite asserts that ExchangeRate complies to expected business rules.
 */
class ExchangeRateTest {

    private val usdToEurRate = ExchangeRate(usDollar, euro, BigDecimal.valueOf(0.89), LocalDateTime.now())
    private val usdToBrlRate = ExchangeRate(usDollar, brazilianReal, BigDecimal.valueOf(3.94), LocalDateTime.now())
    private val brlToUsdRate = ExchangeRate(brazilianReal, usDollar, BigDecimal.valueOf(0.25), LocalDateTime.now())
    private val brlToEurRate = ExchangeRate(brazilianReal, euro, BigDecimal.valueOf(0.25), LocalDateTime.now())

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