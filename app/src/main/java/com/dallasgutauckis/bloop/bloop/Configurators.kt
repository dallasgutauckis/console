package com.dallasgutauckis.bloop.bloop

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import rx.Observable

/**
 * Created by dallas on 2017-10-28.
 */
class Configurators(private val context: Context) {
    fun configurableApps(): Observable<ResolveInfo> {
        return Observable.from(context.packageManager.queryBroadcastReceivers(Intent("configurator.intent.ACTION"), PackageManager.GET_META_DATA))
    }

    fun getConfiguredApps() {
    }
}