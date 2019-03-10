package com.dallasgutauckis.bloop.bloop

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import com.dallasgutauckis.configurator.shared.Signing
import com.dallasgutauckis.configurator.shared.model.Config
import com.dallasgutauckis.configurator.shared.model.Node
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun blah() {
        val config = Config("com.seatgeek.android",
                listOf(
                        Node.createTab("network", "Network", listOf(
                                Node.createSwitch("example_switch", "Example switch", "Switch description", false),
                                Node.createSwitch("example_switch", "Example switch on", null, enabled = true)

                        )),
                        Node.createTab("misc", "Miscellaneous", listOf(
                                Node.createEditText("hello_world", "Hello world text", "")
                        ))
                )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        blah()

        configuredAppsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        configuredAppsRecyclerView.adapter = ConfiguredAppsAdapter(configuredAppsList, object : ConfiguredAppsAdapter.EventListener {
            override fun onItemLongClick(item: AvailableApp, view: ConfiguredAppItemView): Boolean {
                Signing.removeKeyPair(item.packageName)

                val removeIndex = configuredAppsList.indexOf(item)
                configuredAppsList.remove(item)
                configuredAppsRecyclerView.adapter.notifyItemRemoved(removeIndex)

                unconfiguredAppsList.add(item)
                unconfiguredAppsRecyclerView.adapter.notifyItemInserted(unconfiguredAppsList.indexOf(item))

                return true
            }

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

                val dialog = AlertDialog.Builder(this@MainActivity, R.style.Theme_AppCompat_Dialog)
                        .setTitle(item.appTitle)
                        .setIcon(item.appIcon)
                        .setMessage("your public key for ${item.packageName}: $publicKeyBase64; also printed to logcat")

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.forEach {
                        log("item: $it")
                        log("sig: ${Base64.encode(Signing.getPublicKey(it.packageName).encoded, Base64.NO_WRAP).toString(charset("utf-8"))}")
                    }
                    configuredAppsList.clear()
                    configuredAppsList.addAll(it)
                    configuredAppsRecyclerView.adapter.notifyDataSetChanged()
                }

        unconfiguredAppsRelay
                .subscribe {
                    unconfiguredAppsList.clear()
                    unconfiguredAppsList.addAll(it)
                    unconfiguredAppsRecyclerView.adapter.notifyDataSetChanged()
                }

        Configurators(packageManager)
                .configuredApps()
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(configuredAppsRelay)

        Configurators(packageManager)
                .unconfiguredApps()
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(unconfiguredAppsRelay)
    }
}
