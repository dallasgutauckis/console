package com.dallasgutauckis.henson

import android.content.Intent
import android.content.pm.PackageManager
import com.dallasgutauckis.configurator.shared.Signing
import com.dallasgutauckis.henson.config.model.AvailableApp
import io.reactivex.Observable

class Muppets(private val packageManager: PackageManager) {
    fun getBindIntent(packageName: String): Intent {
        val intent = Intent()
        intent.setClassName(packageName, "com.dallasgutauckis.henson.configurator.ConfigurationService")
        return intent
    }

    fun configurableApps(): List<AvailableApp> {
        return packageManager.queryBroadcastReceivers(Intent("configurator.intent.ACTION"), PackageManager.GET_META_DATA)
                .map { AvailableApp(packageManager.getApplicationIcon(it.activityInfo.applicationInfo), it.activityInfo.loadLabel(packageManager).toString(), it.activityInfo.packageName) }
    }

    fun configuredApps(): List<AvailableApp> {
        return configurableApps().filter { Signing.hasKeyPair(it.packageName) }
    }

    fun unconfiguredApps(): List<AvailableApp> {
        return configurableApps().filter { !Signing.hasKeyPair(it.packageName) }
    }
}