package com.dallasgutauckis.henson.service

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.dallasgutauckis.henson.ExternalAppConfigurationActivity
import com.dallasgutauckis.henson.MainActivity
import com.dallasgutauckis.henson.R

@TargetApi(Build.VERSION_CODES.N)
class HensonTileService : TileService() {
    private val TAG = this.javaClass.simpleName
    private val CHANNEL_ID = "foreground_service_notification"
    private val NOTIFICATION_ID = 1

    private var isInForeground: Boolean = false

    override fun onClick() {
        Log.v(TAG, "onClick called")
        if (!isInForeground) {
            // TODO is this legit how this is supposed to be done? this seems dumb (creating the channels as they are used…)
            createNotificationChannel()
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setContentTitle("Henson")
                    .setContentText("Henson content text")
                    .setSmallIcon(R.drawable.ic_notif_remote)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .build()

            // start a foreground service?
            startForeground(NOTIFICATION_ID, notification)

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            updateTileState(Tile.STATE_ACTIVE)
        } else {
            stopForeground(true)
            updateTileState(Tile.STATE_INACTIVE)
        }

        isInForeground = !isInForeground
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateTileState(newState: Int) {
        val tile = qsTile
        tile.state = newState
        tile.updateTile()
    }
}
