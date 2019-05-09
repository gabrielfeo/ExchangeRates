package com.gabrielfeo.exchangerates.app.infrastructure.rates.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @property currency the fixed currency of [exchangeRatesMap]
 * @property time the time of the [exchangeRatesMap] valuation
 * @property exchangeRatesMap map of variable currency codes to a monetary value. Each entry's value corresponds to 1.00
 * in [currency] converted to the variable currency of the same map entry.
 */
data class ExchangeRatesResponse(
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("time")
    val time: String,
    @JsonProperty("exchangeRates")
    val exchangeRatesMap: Map<String, Double>
)