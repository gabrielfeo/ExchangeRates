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

class ExchangeRatesViewModel : ViewModel(), KoinComponent {

    val availableFixedCurrencies: LiveData<Iterable<String>>
        get() = _availableFixedCurrencies

    val availableVariableCurrencies: LiveData<Iterable<String>>
        get() = _availableFixedCurrencies

    val exchangeRate: LiveData<String>
        get() = _exchangeRate

    suspend fun refreshExchangeRates(fixedCurrencyCode: String, variableCurrencyCode: String) {
        val fixedCurrency = currencyUnitRepository[fixedCurrencyCode]
        val variableCurrency = currencyUnitRepository[variableCurrencyCode]
        if (fixedCurrency != null && variableCurrency != null) {
            val rate = exchangeRatesRepository.getRate(fixedCurrency, variableCurrency)
            val roundedRateValue = rate.value.round(MathContext(3, RoundingMode.HALF_UP))
            _exchangeRate.postValue(roundedRateValue.toString())
        }
    }


    private val currencyUnitRepository by inject<CurrencyUnitRepository>()
    private val exchangeRatesRepository by inject<ExchangeRateRepository>()

    private val _availableFixedCurrencies = MutableLiveData<Iterable<String>>()
    private val _availableVariableCurrencies = MutableLiveData<Iterable<String>>()
    private val _exchangeRate = MutableLiveData<String>()

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