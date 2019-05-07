@file:Suppress("RemoveExplicitTypeArguments")

package com.gabrielfeo.exchangerates.app.view

import com.gabrielfeo.exchangerates.app.infrastructure.RetrofitFactory
import com.gabrielfeo.exchangerates.app.infrastructure.currency.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.app.infrastructure.rates.CachedExchangeRateRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import org.koin.dsl.module.module
import retrofit2.Retrofit

internal val infrastructureModule
    get() = module {

        single<Retrofit> {
            val shouldLog: Boolean = BuildConfig.DEBUG
            RetrofitFactory().createRetrofit(shouldLog)
        }

        module("DomainInfrastructure") {
            single<ExchangeRateRepository> { CachedExchangeRateRepository(retrofit = get<Retrofit>()) }
            single<CurrencyUnitRepository> { JodaCurrencyUnitRepository() }
        }

    }