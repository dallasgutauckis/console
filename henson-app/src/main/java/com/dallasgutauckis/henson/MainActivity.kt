package com.dallasgutauckis.henson

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dallasgutauckis.configurator.shared.Signing
import com.dallasgutauckis.configurator.shared.model.Config
import com.dallasgutauckis.configurator.shared.model.Node
import com.dallasgutauckis.henson.config.model.AvailableApp
import kotterknife.bindView
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val configuredAppsRecyclerView: RecyclerView by bindView(R.id.configured_apps)
    val unconfiguredAppsRecyclerView: RecyclerView by bindView(R.id.unconfigured_apps)

    val configuredAppsList = ArrayList<AvailableApp>()
    val unconfiguredAppsList = ArrayList<AvailableApp>()

    private fun log(message: String) {
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

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        blah()

        setUpConfiguredApps(Muppets(packageManager).configuredApps())
        setUpUnconfiguredApps(Muppets(packageManager).unconfiguredApps())
    }

    private fun setUpUnconfiguredApps(unconfiguredApps: List<AvailableApp>) {
        unconfiguredAppsList.addAll(unconfiguredApps)

        unconfiguredAppsRecyclerView.layoutManager = LinearLayoutManager(this)
        unconfiguredAppsRecyclerView.adapter = UnconfiguredAppsAdapter(unconfiguredAppsList, object : UnconfiguredAppsAdapter.EventListener {
            override fun onItemClick(item: AvailableApp, view: UnconfiguredAppItemView) {
                val encodedPublicKey = Signing.generateKeyPair(item.packageName).encoded
                val publicKeyBase64 = Base64.encode(encodedPublicKey, Base64.DEFAULT).toString(charset("utf-8"))

                log("public key: $publicKeyBase64")

                val dialog = AlertDialog.Builder(this@MainActivity, R.style.Theme_AppCompat_Dialog)
                        .setTitle(item.appTitle)
                        .setIcon(item.appIcon)
                        .setMessage("your public key for ${item.packageName}: $publicKeyBase64; also printed to logcat")

                dialog.show()


                val indexRemoved = unconfiguredAppsList.indexOf(item)
                unconfiguredAppsList.remove(item)
                unconfiguredAppsRecyclerView.adapter!!.notifyItemRemoved(indexRemoved)

                configuredAppsList.add(item)
                val indexAdded = configuredAppsList.indexOf(item)
                configuredAppsRecyclerView.adapter!!.notifyItemInserted(indexAdded)
            }
        })
    }

    private fun setUpConfiguredApps(configuredApps: List<AvailableApp>) {
        configuredAppsList.addAll(configuredApps)

        configuredAppsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        configuredAppsRecyclerView.adapter = ConfiguredAppsAdapter(configuredAppsList, object : ConfiguredAppsAdapter.EventListener {
            override fun onItemLongClick(item: AvailableApp, view: ConfiguredAppItemView): Boolean {
                Signing.removeKeyPair(item.packageName)

                val removeIndex = configuredAppsList.indexOf(item)
                configuredAppsList.remove(item)
                configuredAppsRecyclerView.adapter!!.notifyItemRemoved(removeIndex)

                unconfiguredAppsList.add(item)
                unconfiguredAppsRecyclerView.adapter!!.notifyItemInserted(unconfiguredAppsList.indexOf(item))

                return true
            }

            override fun onItemClick(item: AvailableApp, view: ConfiguredAppItemView) {
                val intent = Intent(this@MainActivity, ExternalAppConfigurationActivity::class.java)
                intent.putExtra(ExternalAppConfigurationActivity.EXTRA_PACKAGE_NAME, item.packageName)
                startActivity(intent)
            }
        })
    }
}
