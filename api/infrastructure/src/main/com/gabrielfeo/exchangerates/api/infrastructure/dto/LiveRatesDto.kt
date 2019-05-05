package com.gabrielfeo.exchangerates.api.infrastructure.dto

internal data class LiveRatesDto(
    val success: Boolean = false,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double> = emptyMap()
)