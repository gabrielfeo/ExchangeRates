package com.gabrielfeo.exchangerates.api.application.rates.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRate

data class RatesResponse(
    val currency: String,
    val time: String,
    @JsonIgnore val exchangeRates: Collection<ExchangeRate>
) {

    val serializedRates: Map<String, Any>
        @JsonAnyGetter
        get() = mapOf(
            "exchangeRates" to exchangeRates.associate { rate -> rate.variableCurrency.code to rate.value.toDouble() }
        )

}