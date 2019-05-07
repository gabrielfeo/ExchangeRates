@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view.rates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.gabrielfeo.exchangerates.app.view.R
import com.gabrielfeo.exchangerates.app.view.databinding.ExchangeRatesActivityBinding

class ExchangeRatesActivity : AppCompatActivity() {

    private val viewModel = ViewModelProviders.of(this).get<ExchangeRatesViewModel>()
    private lateinit var binding: ExchangeRatesActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.exchange_rates_activity)
    }

}