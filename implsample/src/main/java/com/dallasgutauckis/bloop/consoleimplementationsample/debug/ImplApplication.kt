package com.dallasgutauckis.bloop.consoleimplementationsample.debug

import android.app.Application
import com.dallasgutauckis.bloop.configurator.Configurator
import com.dallasgutauckis.bloop.configurator.logger.LogcatLogger


class ImplApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Configurator.logger = LogcatLogger()
        Configurator.autoConfigure(this)
    }
}