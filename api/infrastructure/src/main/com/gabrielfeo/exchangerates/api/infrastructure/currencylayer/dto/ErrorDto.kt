package com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.gabrielfeo.exchangerates.api.infrastructure.currencylayer.exception.ApiException

internal class ErrorDto {

    var code: Int = -1
        private set
    var info: String = ""
        private set

    fun toException(): ApiException =
        if (code != -1 && info.isNotEmpty()) ApiException(code, info)
        else ApiException.unknown

    @JsonProperty("error")
    private fun unpackErrorObjectMembers(json: Map<String, Any>) {
        (json["code"] as? Int)
            ?.let { code = it }
        (json["info"] as? String)
            ?.let { info = it }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ErrorDto) this.code == other.code
        else false
    }

    override fun hashCode(): Int {
        return this.code.hashCode()
    }

    override fun toString(): String {
        return "ErrorDto(code=$code, info='$info')"
    }


}