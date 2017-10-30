package com.dallasgutauckis.bloop.consoleimplementationsample.debug

import android.app.Application
import com.dallasgutauckis.bloop.configurator.Configurator
import com.dallasgutauckis.bloop.configurator.logger.LogcatLogger


class ImplApplication : Application(), Configurator.ConfigListener {
    var lastValue: String = "Hello world"

    override fun onConfigUpdated(key: String, value: String) {
        lastValue = value
    }

    override fun onCreate() {
        super.onCreate()

        Configurator.logger = LogcatLogger()
        Configurator.autoConfigure(this)
        Configurator.configListener = this
    }
}