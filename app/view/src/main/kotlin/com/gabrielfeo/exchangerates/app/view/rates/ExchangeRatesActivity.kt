@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.gabrielfeo.exchangerates.app.view.R
import com.gabrielfeo.exchangerates.app.view.databinding.ExchangeRatesActivityBinding

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

}