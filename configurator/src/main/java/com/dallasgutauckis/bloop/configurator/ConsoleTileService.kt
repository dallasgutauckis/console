package com.dallasgutauckis.bloop.configurator

import android.annotation.TargetApi
import android.os.Build
import android.service.quicksettings.TileService
import android.util.Log

@TargetApi(Build.VERSION_CODES.N)
class ConsoleTileService : TileService() {
    // TODO this tile isn't appearing; find out why

    private val TAG = this.javaClass.simpleName

    override fun onCreate() {
        Log.v(TAG, "onCreate")
        super.onCreate()
    }

    override fun onClick() {
        Log.v(TAG, "Tile clicked")
        qsTile.label = "${qsTile.label}a"
        qsTile.updateTile()
    }
}
