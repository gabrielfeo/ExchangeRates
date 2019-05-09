@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view

import com.gabrielfeo.exchangerates.app.infrastructure.RetrofitFactory
import com.gabrielfeo.exchangerates.app.infrastructure.currency.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.app.infrastructure.rates.CachedExchangeRateRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit

internal val infrastructureModule
    get() = module {

        single<Retrofit> {
            val baseUrl = androidApplication().getString(R.string.exchange_rates_api_url)
            val shouldLog: Boolean = BuildConfig.DEBUG
            RetrofitFactory().createRetrofit(baseUrl, shouldLog)
        }

        module("DomainInfrastructure") {
            single<CurrencyUnitRepository> { JodaCurrencyUnitRepository() }
            single<ExchangeRateRepository> {
                CachedExchangeRateRepository(get<CurrencyUnitRepository>(), get<Retrofit>())
            }
        }

    }