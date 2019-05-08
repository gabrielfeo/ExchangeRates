package com.gabrielfeo.exchangerates.app.view.rates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime

class ExchangeRatesViewModel : ViewModel(), KoinComponent {

    val availableFixedCurrencies: LiveData<Collection<String>>
        get() = _availableFixedCurrencies

    val availableVariableCurrencies: LiveData<Collection<String>>
        get() = _availableFixedCurrencies

    val exchangeRate: LiveData<Map<LocalDateTime, String>>
        get() = _exchangeRate

    suspend fun refreshExchangeRates(fixedCurrencyCode: String, variableCurrencyCode: String) {
        val fixedCurrency = currencyUnitRepository[fixedCurrencyCode]
        val variableCurrency = currencyUnitRepository[variableCurrencyCode]
        if (fixedCurrency != null && variableCurrency != null) {
            val rate = exchangeRatesRepository.getRate(fixedCurrency, variableCurrency)
            val roundedRateValue = rate.value.round(MathContext(3, RoundingMode.HALF_UP))
            _exchangeRate.postValue(mapOf(rate.time to roundedRateValue.toString()))
        }
    }


    private val currencyUnitRepository by inject<CurrencyUnitRepository>()
    private val exchangeRatesRepository by inject<ExchangeRateRepository>()

    private val _availableFixedCurrencies = MutableLiveData<Collection<String>>()
    private val _availableVariableCurrencies = MutableLiveData<Collection<String>>()
    private val _exchangeRate = MutableLiveData<Map<LocalDateTime, String>>()

    init {
        setAvailableFixedCurrencies()
        setAvailableVariableCurrencies()
    }

    private fun setAvailableFixedCurrencies() {
        currencyUnitRepository["USD"]
            ?.let { usDollar -> _availableFixedCurrencies.postValue(listOf(usDollar.code)) }
    }

    private fun setAvailableVariableCurrencies() {
        currencyUnitRepository
            .currencies
            .filterNot { it.code == "USD" }
            .map { currency -> currency.code }
            .let { availableCodes -> _availableVariableCurrencies.postValue(availableCodes) }
    }

}