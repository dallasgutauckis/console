package com.dallasgutauckis.bloop.configurator.logger


interface Logger {
    fun v(tag: String, message: Any?)
    fun w(tag: String, message: Any?)
    fun e(tag: String, message: Any?)
    fun e(tag: String, message: Any?, throwable: Throwable?)
}