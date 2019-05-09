@file:Suppress("MoveLambdaOutsideParentheses")

package com.gabrielfeo.exchangerates.api.application

import com.gabrielfeo.exchangerates.api.application.rates.rates
import io.ktor.application.Application

fun Application.main() {
    rates()
}