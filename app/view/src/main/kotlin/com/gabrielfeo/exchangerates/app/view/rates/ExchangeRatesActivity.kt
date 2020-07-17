@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.gabrielfeo.exchangerates.app.view.R
import com.gabrielfeo.exchangerates.app.view.databinding.ExchangeRatesActivityBinding
import com.gabrielfeo.exchangerates.app.view.rates.ExchangeRatesViewModel.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.widget.itemSelections
import reactivecircus.flowbinding.swiperefreshlayout.refreshes

class ExchangeRatesActivity : AppCompatActivity() {

    private val viewModel: ExchangeRatesViewModel by viewModel()
    private lateinit var binding: ExchangeRatesActivityBinding

    private val fixedCurrencyAdapter by lazy { createDefaultCurrencyAdapter() }
    private val variableCurrencyAdapter by lazy { createDefaultCurrencyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.exchange_rates_activity)
        setupExchangeRateChart()
        viewModel.getState(getEvents())
            .asLiveData()
            .observe(owner = this, onChanged = ::setState)
    }

    private fun setState(state: State) {
        when (state.selectorState) {
            CurrencySelectorState.Loading -> { /* TODO Loading view */ }
            CurrencySelectorState.Error -> showGenericError()
            is CurrencySelectorState.Loaded -> with(state.selectorState) {
                fixedCurrencyAdapter.setCurrencies(fixedCurrencies)
                variableCurrencyAdapter.setCurrencies(variableCurrencies)
                binding.apply {
                    ensureAdaptersSet()
                    fixedCurrencySelector.setSelection(selectedFixedCurrencyIndex)
                    variableCurrencySelector.setSelection(selectedVariableCurrencyIndex)
                }
            }
        }
        binding.swipeToRefresh.isRefreshing = state.chartState is ChartState.Refreshing
        when (state.chartState) {
            ChartState.Loading -> { /* TODO Loading view */ }
            ChartState.Refreshing -> { /* Handled above */ }
            ChartState.Error -> showGenericError()
            is ChartState.ShowingChart -> {
                val dataPoints = state.chartState.dataPoints
                val chartEntries = dataPoints.map { (x, y) -> Entry(x, y) }
                val chartData = LineData(LineDataSet(chartEntries, "Exchange rates"))
                binding.exchangeRateChart.data = chartData
            }
        }
    }

    private fun getEvents(): Flow<Event> = with(binding) {
        merge(
            fixedCurrencySelector.currencySelections(),
            variableCurrencySelector.currencySelections(),
            swipeToRefresh.refreshes().map { Event.Refresh(parseCurrentSelection()) }
        )
    }


    private fun ArrayAdapter<String>.setCurrencies(currencies: List<String>) {
        clear()
        addAll(currencies)
    }

    private fun AdapterView<*>.currencySelections(): Flow<Event.ChangeCurrencies> = itemSelections()
        .map { position -> getItemAtPosition(position) as String }
        .map { changedCurrency ->
            val newSelection = parseNewSelection(this, changedCurrency)
            Event.ChangeCurrencies(newSelection)
        }

    private fun parseNewSelection(
        changedSelector: AdapterView<*>,
        changedCurrency: String
    ): CurrencyCodeSelection = with(binding) parse@{
        if (changedSelector == fixedCurrencySelector) {
            val otherCurrency = variableCurrencySelector.selectedItem as String
            CurrencyCodeSelection(fixed = changedCurrency, variable = otherCurrency)
        } else {
            val otherCurrency = fixedCurrencySelector.selectedItem as String
            CurrencyCodeSelection(fixed = otherCurrency, variable = changedCurrency)
        }
    }

    private fun parseCurrentSelection(): CurrencyCodeSelection = with(binding) parse@{
        val variable = variableCurrencySelector.selectedItem as String
        val fixed = fixedCurrencySelector.selectedItem as String
        return@parse CurrencyCodeSelection(fixed, variable)
    }

    private fun createDefaultCurrencyAdapter(): ArrayAdapter<String> {
        return ArrayAdapter<String>(this, android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun setupExchangeRateChart() {
        binding.exchangeRateChart.apply {
            setNoDataTextColor(getColor(R.color.secondaryTextColor))
            setNoDataText(getString(R.string.exchange_rates_no_rates_message))
        }
    }

    private fun showGenericError() {
        val message = getString(R.string.exchange_rates_error_message)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Ensures the lazy adapter are set to their respective views.
     */
    private fun ExchangeRatesActivityBinding.ensureAdaptersSet() {
        fun Spinner.setIfNotSet(adapter: ArrayAdapter<String>) {
            if (getAdapter() == null)
                setAdapter(adapter)
        }
        fixedCurrencySelector.setIfNotSet(fixedCurrencyAdapter)
        variableCurrencySelector.setIfNotSet(variableCurrencyAdapter)
    }

}