@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
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
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.availableFixedCurrencies.observe(this, Observer { rate -> binding.placeholder.text = rate.first() })
    }

}