package com.gabrielfeo.exchangerates.app.view.rates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.math.MathContext
import java.math.RoundingMode
import java.time.OffsetDateTime

class ExchangeRatesViewModel : ViewModel(), KoinComponent {

    val availableFixedCurrencies: LiveData<Collection<String>>
        get() = _availableFixedCurrencies

    val availableVariableCurrencies: LiveData<Collection<String>>
        get() = _availableVariableCurrencies

    /**
     * Map of timestamps to exchange rate values (i.e. x and y), for building a chart.
     */
    val exchangeRate: LiveData<Map<Float, Float>>
        get() = _exchangeRate

    enum class Error {
        EXCHANGE_RATES
    }

    val errors: LiveData<Error>
        get() = _errors

    suspend fun refreshExchangeRates(
        fixedCurrencyCode: String,
        variableCurrencyCode: String
    ) = withContext(Dispatchers.IO) {
        try {
            val exchangeRates = requestExchangeRatesFromRepository(fixedCurrencyCode, variableCurrencyCode)
            val chartEntries = formatAsChartEntries(exchangeRates)
            _exchangeRate.postValue(chartEntries)
        } catch (error: Throwable) {
            Log.e(TAG, "Couldn't refresh exchange rates.", error)
            _errors.postValue(Error.EXCHANGE_RATES)
        }
    }

    private suspend fun requestExchangeRatesFromRepository(
        fixedCurrencyCode: String,
        variableCurrencyCode: String
    ): ExchangeRate {
        val fixedCurrency = currencyUnitRepository[fixedCurrencyCode]
        val variableCurrency = currencyUnitRepository[variableCurrencyCode]
        return if (fixedCurrency != null && variableCurrency != null)
            exchangeRatesRepository.getRate(fixedCurrency, variableCurrency)
        else throw IllegalArgumentException("One or both currencies were null.")
    }

    private fun formatAsChartEntries(rate: ExchangeRate): Map<Float, Float> {
        val zoneOffset = OffsetDateTime.now().offset
        val timestamp = rate.time.toEpochSecond(zoneOffset).toFloat()
        val roundedRateValue = rate.value.round(MathContext(3, RoundingMode.HALF_UP))
        val rateAsFloat = roundedRateValue.toFloat()
        return mapOf(timestamp to rateAsFloat)
    }



    private val currencyUnitRepository by inject<CurrencyUnitRepository>()
    private val exchangeRatesRepository by inject<ExchangeRateRepository>()

    private val _availableFixedCurrencies = MutableLiveData<Collection<String>>()
    private val _availableVariableCurrencies = MutableLiveData<Collection<String>>()
    private val _exchangeRate = MutableLiveData<Map<Float, Float>>()
    private val _errors = MutableLiveData<Error>()

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

private val TAG = ExchangeRatesViewModel::class.simpleName ?: ""