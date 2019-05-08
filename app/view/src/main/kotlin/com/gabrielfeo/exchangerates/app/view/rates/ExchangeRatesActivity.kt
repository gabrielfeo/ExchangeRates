@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.gabrielfeo.exchangerates.app.view.R
import com.gabrielfeo.exchangerates.app.view.databinding.ExchangeRatesActivityBinding
import com.gabrielfeo.exchangerates.app.view.rates.ExchangeRatesViewModel.Error
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExchangeRatesActivity : AppCompatActivity() {

    private lateinit var viewModel: ExchangeRatesViewModel
    private lateinit var binding: ExchangeRatesActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get<ExchangeRatesViewModel>()
        binding = DataBindingUtil.setContentView(this, R.layout.exchange_rates_activity)
        setupFixedCurrencySelector()
        setupVariableCurrencySelector()
        setupExchangeRateChart()
        observeNewCurrenciesSelection()
        observeErrors()
        observeSwipeToRefresh()
    }

    private fun setupFixedCurrencySelector() {
        val currencyAdapter = createDefaultCurrencyAdapter()
        binding.fixedCurrencySelector.adapter = currencyAdapter
        viewModel.availableFixedCurrencies.observe(this, Observer { currencies ->
            currencyAdapter.clear()
            currencyAdapter.addAll(currencies)
        })
    }

    private fun setupVariableCurrencySelector() {
        val currencyAdapter = createDefaultCurrencyAdapter()
        binding.variableCurrencySelector.adapter = currencyAdapter
        viewModel.availableVariableCurrencies.observe(this, Observer { currencies ->
            currencyAdapter.clear()
            currencyAdapter.addAll(currencies)
        })
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
        viewModel.exchangeRate.observe(this, Observer { timesAndRates ->
            val chartEntries = timesAndRates.map { (timestamp, rate) -> Entry(timestamp, rate) }
            val chartData = LineData(LineDataSet(chartEntries, "Exchange rates"))
            binding.exchangeRateChart.data = chartData
        })
    }

    private fun observeNewCurrenciesSelection() {
        binding.fixedCurrencySelector.onItemSelectedListener = onNewCurrencySelectedListener
        binding.variableCurrencySelector.onItemSelectedListener = onNewCurrencySelectedListener
    }

    private val onNewCurrencySelectedListener = object : AdapterView.OnItemSelectedListener {
        var fixedCurrency: String = ""
        var variableCurrency: String = ""
        fun refreshSelection() = notifyViewModelOfSelectedCurrencies(fixedCurrency, variableCurrency)
        override fun onNothingSelected(adapterView: AdapterView<*>?) = adapterView?.setSelection(0) ?: Unit
        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedCurrency = adapterView?.getItemAtPosition(position) as? String
            when {
                adapterView === binding.fixedCurrencySelector -> fixedCurrency = selectedCurrency ?: ""
                adapterView === binding.variableCurrencySelector -> variableCurrency = selectedCurrency ?: ""
            }
            refreshSelection()
        }
    }

    private fun notifyViewModelOfSelectedCurrencies(fixedCurrency: String, variableCurrency: String) {
        if (fixedCurrency.isNotEmpty() && variableCurrency.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.refreshExchangeRates(fixedCurrency, variableCurrency)
            }
        }
    }

    private fun observeErrors() {
        viewModel.errors.observe(this, Observer { error ->
            when (error) {
                Error.EXCHANGE_RATES -> showError(R.string.exchange_rates_error_message)
                else -> showError(R.string.generic_error_message)
            }
        })
    }

    private fun showError(@StringRes messageId: Int) {
        Snackbar.make(binding.root, getString(messageId), Snackbar.LENGTH_SHORT).show()
    }

    private fun observeSwipeToRefresh() = with(binding.swipeToRefresh) {
        setOnRefreshListener { onNewCurrencySelectedListener.refreshSelection() }
        fun stopRefresh() = let { isRefreshing = false }
        viewModel.exchangeRate.observe(this@ExchangeRatesActivity, Observer {
            if (isRefreshing) stopRefresh()
        })
        viewModel.errors.observe(this@ExchangeRatesActivity, Observer { error ->
            if (isRefreshing && error == Error.EXCHANGE_RATES) stopRefresh()
        })
    }

}