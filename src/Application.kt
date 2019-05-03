@file:Suppress("MoveLambdaOutsideParentheses")

package com.gabrielfeo.exchangerates

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        jackson()
    }

    val client = HttpClient(Apache, {})

    routing {
        install(StatusPages) {
            exception<Exception> { call.respond(HttpStatusCode.InternalServerError) }
        }
    }
}
