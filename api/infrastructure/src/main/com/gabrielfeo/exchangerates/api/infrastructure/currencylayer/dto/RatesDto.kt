package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal sealed class RatesDto {

    data class LiveRates(
        val success: Boolean = false,
        val timestamp: Long,
        val source: String,
        val quotes: Map<String, Double> = emptyMap()
    ) : RatesDto()


    data class HistoricalRates(
        val success: Boolean = false,
        val historical: Boolean,
        val date: String,
        val timestamp: Long,
        val source: String,
        val quotes: Map<String, Double> = emptyMap()
    ) : RatesDto()


    class Error : RatesDto() {

        var code: Int = -1
            private set
        var info: String = ""
            private set

        @JsonProperty("error")
        private fun unpackErrorObjectMembers(json: Map<String, Any>) {
            (json["code"] as? Int)
                ?.let { code = it }
            (json["info"] as? String)
                ?.let { info = it }
        }

        override fun equals(other: Any?): Boolean = if (other is Error) this.code == other.code else false
        override fun hashCode(): Int = code.hashCode()
        override fun toString(): String = "ErrorDto(code=$code, info='$info')"

    }

}
