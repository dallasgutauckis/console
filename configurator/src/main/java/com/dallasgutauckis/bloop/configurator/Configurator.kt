package com.dallasgutauckis.bloop.configurator

import android.content.Context
import android.content.pm.PackageManager
import com.dallasgutauckis.bloop.configurator.logger.Logger
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object Configurator {
    init {
        // use autoConfigure, or manually add signatures via addSignature
    }

    private val TAG = "Configurator"
    private val EXAMPLE_SIGNATURE = "deadbeef"

    private val signatures: MutableList<String> = ArrayList()

    var logger: Logger? = null

    fun addSignature(signature: String) {
        signatures.add(signature)
        logger?.v(TAG, "Added signature: " + signature)
    }

    fun autoConfigure(context: Context) {
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val configuratorUrl = checkNotNull(applicationInfo.metaData.getString("configurator_url")) {
            "configurator_url value is missing from AndroidManifest.xml\n" +
                    "Either add configurator_url as <meta-data /> to the <application /> element, or\n" +
                    "manually configure Configurator using addSignature(String)"
        }

        Observable.just(configuratorUrl)
                .subscribeOn(Schedulers.io())
                .map { url -> Request.Builder().url(url).build() }
                .map { request -> OkHttpClient().newCall(request) }
                .map { call -> call.execute() }
                .subscribe({ response ->
                    if (response.code() == 200) {
                        response.body()?.string()?.let {
                            val jsonArray = JSONArray(it)
                            var i = 0
                            val length = jsonArray.length()

                            while (i < length) {
                                addSignature(jsonArray.getString(i++))
                            }
                        }

                        // Expecting JSON array of Strings
                    } else {
                        TODO("retry")
                    }
                })
        // fetch signatures from configurator_url
    }
}