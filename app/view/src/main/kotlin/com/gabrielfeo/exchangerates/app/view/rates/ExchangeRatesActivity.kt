@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.gabrielfeo.exchangerates.app.view.R
import com.gabrielfeo.exchangerates.app.view.databinding.ExchangeRatesActivityBinding
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
        viewModel.exchangeRate.observe(this, Observer { rate ->
        })
    }

    private fun observeNewCurrenciesSelection() {
        binding.fixedCurrencySelector.onItemSelectedListener = onNewCurrencySelectedListener
        binding.variableCurrencySelector.onItemSelectedListener = onNewCurrencySelectedListener
    }

    private val onNewCurrencySelectedListener = object : AdapterView.OnItemSelectedListener {
        var fixedCurrency: String = ""
        var variableCurrency: String = ""
        override fun onNothingSelected(adapterView: AdapterView<*>?) = adapterView?.setSelection(0) ?: Unit
        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedCurrency = adapterView?.getItemAtPosition(position) as? String
            when {
                adapterView === binding.fixedCurrencySelector -> fixedCurrency = selectedCurrency ?: ""
                adapterView === binding.variableCurrencySelector -> variableCurrency = selectedCurrency ?: ""
            }
            selectedCurrency?.let { notifyViewModelOfSelectedCurrencies(fixedCurrency, variableCurrency) }
        }
    }

    private fun notifyViewModelOfSelectedCurrencies(fixedCurrency: String, variableCurrency: String) {
        if (fixedCurrency.isNotEmpty() && variableCurrency.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.refreshExchangeRates(fixedCurrency, variableCurrency)
            }
        }
    }

}