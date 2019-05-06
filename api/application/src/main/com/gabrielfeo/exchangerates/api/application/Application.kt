@file:Suppress("MoveLambdaOutsideParentheses")

package com.gabrielfeo.exchangerates.api.application

import com.gabrielfeo.exchangerates.api.infrastructure.RemoteExchangeRateRepository
import com.gabrielfeo.exchangerates.api.infrastructure.joda.JodaCurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.CurrencyUnitRepository
import com.gabrielfeo.exchangerates.domain.currency.rate.ExchangeRateRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("EXPERIMENTAL_API_USAGE")
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

            get {
                val parameters = call.request.queryParameters
                val fixedCurrencyCode = parameters["fixed"]
                    ?: throw BadRequestException("Fixed currency must be provided.")
                val variableCurrencyCodes = parameters["variable"]?.split(",")
                    ?: throw BadRequestException("At least one variable currency must be provided.")

                val fixedCurrency = currencyUnitRepository[fixedCurrencyCode]
                    ?: throw BadRequestException("Fixed currency must be a valid ISO 4217 currency code.")
                val variableCurrencies = variableCurrencyCodes
                    .map { code -> currencyUnitRepository[code] }
                    .also {
                        if (it.contains(null))
                            throw BadRequestException("All variable currencies must be valid ISO 4217 currency codes.")
                    }
                    .filterNotNull()

                val rates = exchangeRateRepository.getRates(fixedCurrency, variableCurrencies)
                call.respond(HttpStatusCode.OK, rates) // TODO Define output JSON model
            }

            get("past") {
                // TODO Implement past rates
            }

        }

    }
}
