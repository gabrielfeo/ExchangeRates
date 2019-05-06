package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception

internal class ApiException(
    val errorCode: Int,
    errorInfo: String,
    cause: Throwable? = null
) : RuntimeException(errorInfo, cause) {

    constructor(message: String, cause: Throwable? = null)
            : this(-1, "Generic API error", cause)

    companion object {
        val unknown: ApiException
            get() = ApiException(-1, "Unknown API error")
    }

}