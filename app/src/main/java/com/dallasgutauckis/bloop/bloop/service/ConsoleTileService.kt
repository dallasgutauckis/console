package com.dallasgutauckis.bloop.bloop.service

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import android.util.Log
import com.dallasgutauckis.bloop.bloop.ExternalAppConfigurationActivity

@TargetApi(Build.VERSION_CODES.N)
class ConsoleTileService : TileService() {
    private val TAG = this.javaClass.simpleName

    override fun onClick() {
        Log.v(TAG, "Tile clicked")
        // qsTile.label = "${qsTile.label}a"
        // qsTile.updateTile()
        startActivity(Intent(applicationContext, ExternalAppConfigurationActivity::class.java))
    }
}
