package com.dallasgutauckis.henson.muppet

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dallasgutauckis.configurator.shared.IConfigurationService


class ConfigurationService : Service() {
    val TAG = "ConfigurationService"

    override fun onBind(intent: Intent?): IBinder {
        Log.v(TAG, "binding")
        return binder
    }

    private val binder = object : IConfigurationService.Stub() {
        override fun onMessage(publicKey: ByteArray, jsonPayload: ByteArray, signature: ByteArray): Int {
            Log.v(TAG, "Got message: $jsonPayload")
            return Configurator.onMessage(publicKey, jsonPayload, signature)
        }
    }
}