@file:Suppress("MoveLambdaOutsideParentheses")

package com.gabrielfeo.exchangerates.api.application

import com.gabrielfeo.exchangerates.api.infrastructure.RemoteExchangeRateRepository
import com.gabrielfeo.exchangerates.api.infrastructure.joda.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnit
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import java.time.LocalDate
import java.time.OffsetDateTime

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@KtorExperimentalAPI
fun Application.rates(testing: Boolean = false) {

    val currencyUnitRepository: CurrencyUnitRepository = JodaCurrencyUnitRepository() // TODO Configure DI
    val exchangeRateRepository: ExchangeRateRepository = RemoteExchangeRateRepository(
        currencyLayerApiKey = System.getenv("Currency_Layer_Api_Key") // TODO Find a secret manager
    )

    install(ContentNegotiation) { jackson() }
    install(StatusPages) {
        exception<Exception> { call.respond(HttpStatusCode.InternalServerError) }
    }

    routing {
        route("api/rates") {

            get("current") {
                val parameters = call.request.queryParameters
                val (fixedCurrency, variableCurrencies) = parseCurrenciesFromQuery(parameters, currencyUnitRepository)
                val rates = exchangeRateRepository.getRates(fixedCurrency, variableCurrencies)
                call.respond(HttpStatusCode.OK, rates) // TODO Define output JSON model
            }

            get("past") {
                val parameters = call.request.queryParameters
                val (fixedCurrency, variableCurrencies) = parseCurrenciesFromQuery(parameters, currencyUnitRepository)
                val time = parseTimeFromQuery(parameters)
                val rates = exchangeRateRepository.getRatesAt(time, fixedCurrency, variableCurrencies)
                call.respond(HttpStatusCode.OK, rates)
            }

        }
    }

}

private fun parseCurrenciesFromQuery(
    parameters: Parameters,
    currencyUnitRepository: CurrencyUnitRepository
): Pair<CurrencyUnit, List<CurrencyUnit>> {
    val (fixedCurrencyCode, variableCurrencyCodes) = parseCurrencyParameterValues(parameters)
    val fixedCurrency = currencyUnitRepository[fixedCurrencyCode]
        ?: throw BadRequestException("Fixed currency must be a valid ISO 4217 currency code.")
    val variableCurrencies = variableCurrencyCodes
        .map { code -> currencyUnitRepository[code] }
        .also {
            if (it.contains(null))
                throw BadRequestException("All variable currencies must be valid ISO 4217 currency codes.")
        }
        .filterNotNull()

    return Pair(fixedCurrency, variableCurrencies)
}

private fun parseCurrencyParameterValues(parameters: Parameters): Pair<String, List<String>> {
    val fixedCurrencyCode = parameters["fixed"]
        ?: throw BadRequestException("Fixed currency must be provided.")
    val variableCurrencyCodes = parameters["variable"]?.split(",")
        ?: throw BadRequestException("At least one variable currency must be provided.")
    return Pair(fixedCurrencyCode, variableCurrencyCodes)
}

private fun parseTimeFromQuery(parameters: Parameters): OffsetDateTime {
    val timeString = parameters["time"]
        ?: throw BadRequestException("A time must be provided to get past exchange rates")
    return runCatching {
        OffsetDateTime.parse(timeString)
    }.getOrElse {
        val parsedDate = LocalDate.parse(timeString)
        val zoneOffset = OffsetDateTime.now().offset
        parsedDate.atTime(0, 0).atOffset(zoneOffset)
    }

}