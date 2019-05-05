package com.gabrielfeo.exchangerates.api.infrastructure.dto

internal data class HistoricalRatesDto(
    val success: Boolean = false,
    val historical: Boolean,
    val date: String,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double> = emptyMap()
)