package com.dallasgutauckis.bloop.bloop

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.widget.TextView
import com.dallasgutauckis.bloop.bloop.AvailableAppsAdapter.EventListener
import com.dallasgutauckis.configurator.shared.Signing
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val availableApps: RecyclerView by bindView(R.id.available_apps)

    val configuredApps = PublishRelay.create<Collection<AvailableApp>>()
    val configurableApps = PublishRelay.create<Collection<AvailableApp>>()

    val list = ArrayList<AvailableApp>()

    fun log(message: String) {
        Log.v(TAG, message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        availableApps.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        availableApps.adapter = AvailableAppsAdapter(list, object : EventListener {
            override fun onCardClick(item: AvailableApp, view: AvailableAppItemView) {
                val alias = "app/${item.packageName}"
                val encodedPublicKey = if (Signing.hasKeyPair(alias)) {
                    Signing.getPublicKey(alias)
                } else {
                    Signing.generateKeyPair(alias)
                }.encoded

                val publicKeyBase64 = Base64.encode(encodedPublicKey, Base64.DEFAULT).toString(charset("utf-8"))

                log("public key: " + publicKeyBase64)

                val textView = TextView(this@MainActivity)
                textView.setTextIsSelectable(true)
                textView.text = "your public key for ${item.packageName}: $publicKeyBase64"

                val dialog = AlertDialog.Builder(this@MainActivity, R.style.Theme_AppCompat_Dialog)
                        .setTitle(item.appTitle)
                        .setIcon(item.appIcon)
                        .setView(textView)

                dialog.show()
            }
        })

        configurableApps
                .subscribe {
                        list.clear()
                        list.addAll(it)
                        availableApps.adapter.notifyDataSetChanged()
                }

        Configurators(packageManager)
                .configurableApps()
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(configurableApps)

        // find applications that are configurable
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
