package com.dallasgutauckis.henson.consoleimplementationsample.debug

import android.app.Application
import com.dallasgutauckis.henson.muppet.Configurator
import com.dallasgutauckis.henson.muppet.logger.LogcatLogger


class ImplApplication : Application(), Configurator.ConfigListener {
    var lastValue: String = "Hello world"

    override fun onConfigUpdated(key: String, value: String) {
        lastValue = value
    }

    override fun onCreate() {
        super.onCreate()

        Configurator.logger = LogcatLogger()
        Configurator.configListener = this

        Thread {
            Configurator.autoConfigure(this)
        }.start()

    }
}