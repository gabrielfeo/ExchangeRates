package com.gabrielfeo.exchangerates.app.view.rates

import androidx.lifecycle.ViewModel
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import kotlinx.coroutines.flow.*
import java.math.MathContext
import java.math.RoundingMode
import java.time.OffsetDateTime

class ExchangeRatesViewModel(
    private val currencyUnitRepository: CurrencyUnitRepository,
    private val exchangeRatesRepository: ExchangeRateRepository
) : ViewModel() {

    data class State(
        val selectorState: CurrencySelectorState,
        val chartState: ChartState
    )
    sealed class CurrencySelectorState {
        object Loading : CurrencySelectorState()
        object Error : CurrencySelectorState()
        data class Loaded(
            val fixedCurrencies: List<String>,
            val variableCurrencies: List<String>,
            val selectedFixedCurrencyIndex: Int,
            val selectedVariableCurrencyIndex: Int
        ) : CurrencySelectorState()
    }
    sealed class ChartState {
        object Loading : ChartState()
        object Refreshing : ChartState() // (user-initiated)
        object Error : ChartState()
        data class ShowingChart(val dataPoints: Map<Float, Float>) : ChartState()
    }

    sealed class Event {
        data class Refresh(val currentSelection: CurrencyCodeSelection) : Event()
        data class ChangeCurrencies(val newSelection: CurrencyCodeSelection) : Event()
    }

    data class CurrencyCodeSelection(
        val fixed: String,
        val variable: String
    )


    private inline val currentState get() = state.value
    private val state = MutableStateFlow(State(CurrencySelectorState.Loading, ChartState.Loading))

    fun getState(events: Flow<Event>): Flow<State> =
        merge(state, events.reduceToState())
            .catch { emit(currentState.copy(chartState = ChartState.Error)) }
            .onStart { emit(buildInitialState()) }
            .onEach { state.value = it }
            .distinctUntilChanged()

    private fun Flow<Event>.reduceToState(): Flow<State> = transformLatest<Event, State> { event ->
        val currencySelection = when (event) {
            is Event.Refresh -> event.currentSelection
            is Event.ChangeCurrencies -> event.newSelection
        }
        val newSelectorState = createNewSelectorStateFor(currencySelection)
        emit(currentState.copy(selectorState = newSelectorState, chartState = ChartState.Loading))
        val newChartEntries = getChartEntriesFor(currencySelection)
        emit(currentState.copy(chartState = ChartState.ShowingChart(newChartEntries)))
    }

    private suspend fun buildInitialState(): State {
        val fixedCurrencies = getAvailableFixedCurrencies().map { it.code }
        val variableCurrencies = getAvailableVariableCurrencies().map { it.code }
        val defaultSelection = CurrencyCodeSelection(fixedCurrencies.first(), variableCurrencies.first())
        val chartEntries = getChartEntriesFor(defaultSelection)
        return State(
            CurrencySelectorState.Loaded(fixedCurrencies, variableCurrencies, 0, 0),
            ChartState.ShowingChart(chartEntries)
        )
    }

    private fun createNewSelectorStateFor(newSelection: CurrencyCodeSelection): CurrencySelectorState {
        val current = checkNotNull(currentState.selectorState as? CurrencySelectorState.Loaded)
        return current.copy(
            selectedFixedCurrencyIndex = current.fixedCurrencies.indexOf(newSelection.fixed),
            selectedVariableCurrencyIndex = current.variableCurrencies.indexOf(newSelection.variable)
        )
    }


    private suspend fun getChartEntriesFor(selection: CurrencyCodeSelection): Map<Float, Float> {
        val (fixed, variable) = selection
        val exchangeRates = requestExchangeRatesFromRepository(fixed, variable)
        return formatAsChartEntries(exchangeRates)
    }

    private suspend fun requestExchangeRatesFromRepository(
        fixedCurrencyCode: String,
        variableCurrencyCode: String
    ): ExchangeRate {
        val fixedCurrency = requireNotNull(currencyUnitRepository[fixedCurrencyCode])
        val variableCurrency = requireNotNull(currencyUnitRepository[variableCurrencyCode])
        return exchangeRatesRepository.getRate(fixedCurrency, variableCurrency)
    }

    private fun formatAsChartEntries(rate: ExchangeRate): Map<Float, Float> {
        val zoneOffset = OffsetDateTime.now().offset
        val timestamp = rate.time.toEpochSecond(zoneOffset).toFloat()
        val roundedRateValue = rate.value.round(MathContext(3, RoundingMode.HALF_UP))
        val rateAsFloat = roundedRateValue.toFloat()
        return mapOf(timestamp to rateAsFloat)
    }

    private fun getAvailableFixedCurrencies(): List<CurrencyUnit> {
        return currencyUnitRepository["USD"]
            ?.let { listOf(it) } ?: emptyList()
    }
    private fun getAvailableVariableCurrencies(): List<CurrencyUnit> {
        return currencyUnitRepository.currencies
            .filterNot { it.code == "USD" }
    }


}