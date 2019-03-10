package com.dallasgutauckis.bloop.bloop

import android.content.Intent
import android.content.pm.PackageManager
import com.dallasgutauckis.configurator.shared.Signing
import io.reactivex.Observable

class Configurators(private val packageManager: PackageManager) {
    fun getBindIntent(packageName: String): Intent {
        val intent = Intent()
        intent.setClassName(packageName, "com.dallasgutauckis.bloop.configurator.ConfigurationService")
        return intent
    }

    fun configurableApps(): Observable<AvailableApp> {
        return Observable.fromIterable(packageManager.queryBroadcastReceivers(Intent("configurator.intent.ACTION"), PackageManager.GET_META_DATA))
                .map { AvailableApp(packageManager.getApplicationIcon(it.activityInfo.applicationInfo), it.activityInfo.loadLabel(packageManager).toString(), it.activityInfo.packageName) }
    }

    fun configuredApps(): Observable<AvailableApp> {
        return configurableApps().filter { Signing.hasKeyPair(it.packageName) }
    }

    fun unconfiguredApps(): Observable<AvailableApp> {
        return configurableApps().filter { !Signing.hasKeyPair(it.packageName) }
    }
}