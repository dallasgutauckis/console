package com.dallasgutauckis.henson.muppet.logger

import android.util.Log


open class LogcatLogger : Logger {
    override fun v(tag: String, message: Any?) {
        Log.v(tag, message.toString())
    }

    override fun w(tag: String, message: Any?) {
        Log.w(tag, message.toString())
    }

    override fun e(tag: String, message: Any?) {
        Log.e(tag, message.toString())
    }

    override fun e(tag: String, message: Any?, throwable: Throwable?) {
        Log.e(tag, message.toString(), throwable)
    }

}