package com.gabrielfeo.exchangerates.app.view

import android.app.Application
import org.koin.android.ext.android.startKoin

class ExchangeRatesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(infrastructureModule))
    }

}