package com.dallasgutauckis.bloop.bloop

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.widget.TextView
import com.dallasgutauckis.configurator.shared.Signing
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val configuredAppsRecyclerView: RecyclerView by bindView(R.id.configured_apps)
    val unconfiguredAppsRecyclerView: RecyclerView by bindView(R.id.unconfigured_apps)

    val configuredAppsRelay = PublishRelay.create<Collection<AvailableApp>>()
    val unconfiguredAppsRelay = PublishRelay.create<Collection<AvailableApp>>()

    val configuredAppsList = ArrayList<AvailableApp>()
    val unconfiguredAppsList = ArrayList<AvailableApp>()

    fun log(message: String) {
        Log.v(TAG, message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configuredAppsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        configuredAppsRecyclerView.adapter = ConfiguredAppsAdapter(configuredAppsList, object : ConfiguredAppsAdapter.EventListener {
            override fun onItemClick(item: AvailableApp, view: ConfiguredAppItemView) {
                val intent = Intent(this@MainActivity, ExternalAppConfigurationActivity::class.java)
                intent.putExtra(ExternalAppConfigurationActivity.EXTRA_PACKAGE_NAME, item.packageName)
                startActivity(intent)
            }
        })

        unconfiguredAppsRecyclerView.layoutManager = LinearLayoutManager(this)
        unconfiguredAppsRecyclerView.adapter = UnconfiguredAppsAdapter(unconfiguredAppsList, object : UnconfiguredAppsAdapter.EventListener {
            override fun onItemClick(item: AvailableApp, view: UnconfiguredAppItemView) {
                val encodedPublicKey = Signing.generateKeyPair(item.packageName).encoded
                val publicKeyBase64 = Base64.encode(encodedPublicKey, Base64.DEFAULT).toString(charset("utf-8"))

                log("public key: " + publicKeyBase64)

                val textView = TextView(this@MainActivity)
                textView.setTextIsSelectable(true)
                textView.text = "your public key for ${item.packageName}: $publicKeyBase64; also printed to logcat"

                val dialog = AlertDialog.Builder(this@MainActivity, R.style.Theme_AppCompat_Dialog)
                        .setTitle(item.appTitle)
                        .setIcon(item.appIcon)
                        .setView(textView)

                dialog.show()


                val indexRemoved = unconfiguredAppsList.indexOf(item)
                unconfiguredAppsList.remove(item)
                unconfiguredAppsRecyclerView.adapter.notifyItemRemoved(indexRemoved)

                configuredAppsList.add(item)
                val indexAdded = configuredAppsList.indexOf(item)
                configuredAppsRecyclerView.adapter.notifyItemInserted(indexAdded)
            }
        })

        configuredAppsRelay
                .subscribe {
                    it.forEach {
                        log("item: $it")
                        log("sig: ${Base64.encode(Signing.getPublicKey(it.packageName).encoded, Base64.NO_WRAP).toString(charset("utf-8"))}")
                    }
                    configuredAppsList.clear()
                    configuredAppsList.addAll(it)
                    configuredAppsRecyclerView.adapter.notifyDataSetChanged()
                }

        Configurators(packageManager)
                .configuredApps()
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(configuredAppsRelay)

        // find applications that are configurable
    }
}
