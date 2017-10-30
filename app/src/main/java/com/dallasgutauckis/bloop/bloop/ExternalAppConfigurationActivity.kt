package com.dallasgutauckis.bloop.bloop

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dallasgutauckis.configurator.shared.IConfigurationService
import com.dallasgutauckis.configurator.shared.Signing
import kotterknife.bindView

class ExternalAppConfigurationActivity : AppCompatActivity() {
    companion object {
        val TAG = "ExternalAppConfigurationActivity".substring(0, 22)
        val EXTRA_PACKAGE_NAME: String = "extra_package_name"
    }

    var configurationService: IConfigurationService? = null
    val helloWorldText: EditText by bindView(R.id.hello_world_text)
    val setButton: Button by bindView(R.id.set_button)

    val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.v(TAG, "Disconnected from service")
            configurationService = null
            Toast.makeText(this@ExternalAppConfigurationActivity, "Disconnected", Toast.LENGTH_LONG).show()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.v(TAG, "Connected to service")
            configurationService = IConfigurationService.Stub.asInterface(service)
            Toast.makeText(this@ExternalAppConfigurationActivity, "Connected", Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var configurators: Configurators

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_app_configuration)

        configurators = Configurators(packageManager)

        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)

        val serviceIntent = configurators.getBindIntent(packageName)
        val bindService = bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        setButton.setOnClickListener {
            val signedData = Signing.signData(packageName, helloWorldText.text.toString().toByteArray())
            configurationService!!.onMessage(signedData.encodedPublicKey, signedData.data, signedData.signature)
            // TODO fix this to check for null and make sure there's a connection and all
        }

        Toast.makeText(this@ExternalAppConfigurationActivity, "Bound", Toast.LENGTH_LONG).show()
        Log.v(TAG, "bindService: $bindService")
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }
}
