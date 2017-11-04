package com.dallasgutauckis.configurator.shared

object MessageResponseCode {
    val ERROR_NO_MATCHING_PUBLIC_KEY = 0
    val ERROR_INCORRECTLY_SIGNED_PAYLOAD = 1
    val SUCCESS = 2

    fun isError(code: Int): Boolean {
        return code != SUCCESS
    }

}
