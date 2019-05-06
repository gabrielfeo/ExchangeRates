package com.gabrielfeo.exchangerates.api.infrastructure.tests.integration

import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.CurrencyLayerApi
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.CurrencyLayerApiAgent
import com.gabrielfeo.exchangerates.api.infrastructure.joda.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CurrencyLayerApiAgentTest {

    private val currencyRepository: CurrencyUnitRepository = JodaCurrencyUnitRepository()
    private val agent: CurrencyLayerApi = CurrencyLayerApiAgent(
        apiKey = System.getenv("Currency_Layer_Api_Key"),
        currencyRepository = currencyRepository
    )

    @Test
    fun `Live rates endpoint`() {
        val usd = currencyRepository["USD"]!!
        val brl = currencyRepository["BRL"]!!
        val rates = runBlocking { agent.getLiveRates(usd, listOf(brl)) }
        assertTrue(rates.all { rate -> rate.fixedCurrency == usd })
        assertTrue(rates.all { rate -> rate.variableCurrency == brl })
        assertTrue(rates.all { rate -> rate.time.isAfter(LocalDateTime.now().withYear(2000)) })
    }

    @Test
    fun `Historical rates endpoint`() {
        val usd = currencyRepository["USD"]!!
        val brl = currencyRepository["BRL"]!!
        val date = LocalDate.of(2017, 8, 8)
        val rates = runBlocking { agent.getHistoricalRates(date, usd, listOf(brl)) }
        assertTrue(rates.all { rate -> rate.fixedCurrency == usd })
        assertTrue(rates.all { rate -> rate.variableCurrency == brl })
        assertTrue(rates.all { rate -> rate.time.toLocalDate() == date })
    }

}