package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception

internal class ApiException(
    val errorCode: Int,
    errorInfo: String
) : RuntimeException(message = errorInfo) {

    companion object {
        val unknown: ApiException
            get() = ApiException(-1, "Unknown API error")
    }

}