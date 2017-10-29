package com.dallasgutauckis.bloop.configurator

import android.content.Context
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by dallas on 2017-10-28.
 */
object Configurator {
    init {

    }

    private val signatures: MutableList<String> = ArrayList()

    fun addSignature(signature: String) {
        signatures.add(signature)
    }

    fun autoConfigure(context: Context) {
        val configurator_url = checkNotNull(context.applicationInfo.metaData.getString("configurator_url")) {
            "configurator_url value is missing from AndroidManifest.xml\n" +
                    "Either add configurator_url as <meta-data /> to the <application /> element, or\n" +
                    "manually configure Configurator using addSignature(String)"
        }

        Observable.just(configurator_url)
                .subscribeOn(Schedulers.io())
                .map { url -> Request.Builder().url(url).build() }
                .map { request -> OkHttpClient().newCall(request) }
                .map { call -> call.execute() }
                .subscribe(Consumer { response ->
                    if (response.code() == 200)
                    if (response.body() == null) {

                    }
                    response.body().string()
                })
        // fetch signatures from configurator_url
    }
}