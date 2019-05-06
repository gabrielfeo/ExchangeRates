package com.gabrielfeo.exchangerates.api.infrastructure

import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.CurrencyLayerApi
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.CurrencyLayerApiAgent
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception.ApiException
import com.gabrielfeo.exchangerates.api.infrastructure.joda.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository

class RemoteExchangeRateRepository(currencyLayerApiKey: String) : ExchangeRateRepository {

    private val remote: CurrencyLayerApi = CurrencyLayerApiAgent(currencyLayerApiKey, currencyRepository = JodaCurrencyUnitRepository())

    override suspend fun getRate(fixedCurrency: CurrencyUnit, variableCurrency: CurrencyUnit): ExchangeRate {
        try {
            return remote.getLiveRates(fixedCurrency, listOf(variableCurrency)).first()
        } catch (exception: ApiException) {
            throw RuntimeException(exception) // TODO Create domain exceptions
        }
    }

}