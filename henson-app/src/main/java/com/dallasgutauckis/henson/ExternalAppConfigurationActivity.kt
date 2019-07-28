package com.dallasgutauckis.henson

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dallasgutauckis.configurator.shared.IConfigurationService
import com.dallasgutauckis.configurator.shared.MessageResponseCode
import com.dallasgutauckis.configurator.shared.Signing
import com.google.android.material.snackbar.Snackbar
import kotterknife.bindView

class ExternalAppConfigurationActivity : AppCompatActivity() {
    companion object {
        val TAG = "ExternalAppConfigurationActivity".substring(0, 22)
        val EXTRA_PACKAGE_NAME: String = "extra_package_name"
    }

    var configurationService: IConfigurationService? = null
    val helloWorldText: EditText by bindView(R.id.hello_world_text)
    val setButton: Button by bindView(R.id.set_button)
    val rootView: View by bindView(R.id.activity_root)

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

    private lateinit var muppets: Muppets
    private lateinit var targetPackageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_app_configuration)

        muppets = Muppets(packageManager)
        targetPackageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)


        setButton.setOnClickListener {
            val signedData = Signing.signData(targetPackageName, helloWorldText.text.toString().toByteArray())
            val result = configurationService!!.onMessage(signedData.encodedPublicKey, signedData.data, signedData.signature)

            if (MessageResponseCode.isError(result)) {
                val errorMessage: String = if (result == MessageResponseCode.ERROR_INCORRECTLY_SIGNED_PAYLOAD) {
                    "incorrectly signed payload"
                } else if (result == MessageResponseCode.ERROR_NO_MATCHING_PUBLIC_KEY) {
                    "no matching public key found"
                } else {
                    "unknown error"
                }

                Snackbar.make(rootView, "Error: $errorMessage", Snackbar.LENGTH_LONG).show()
            }

            // TODO fix this to check for null and make sure there's a connection and all that
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = muppets.getBindIntent(targetPackageName)
        val bindService = bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        Toast.makeText(this@ExternalAppConfigurationActivity, "Bound: $bindService", Toast.LENGTH_LONG).show()
        Log.v(TAG, "bindService: $bindService")
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }
}
