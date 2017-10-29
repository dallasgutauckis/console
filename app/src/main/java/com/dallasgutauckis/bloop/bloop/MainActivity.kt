package com.dallasgutauckis.bloop.bloop

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.squareup.whorlwind.SharedPreferencesStorage
import com.squareup.whorlwind.Whorlwind
import kotterknife.bindView
import okio.ByteString
import rx.Observable
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val pinInput: TextView by bindView(R.id.pin_input)
    val saveButton: Button by bindView(R.id.save)
    val loadButton: Button by bindView(R.id.load)

    private lateinit var whorlwind: Whorlwind

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        whorlwind = Whorlwind.create(this, SharedPreferencesStorage(this, "safespace"), "test")

        saveButton.setOnClickListener {
            if (whorlwind.canStoreSecurely()) {
                Observable.just("secret value ❤️")
                        .map { s: String? -> ByteString.encodeUtf8(s) }
                        .subscribeOn(Schedulers.io())
                        .subscribe({ value ->
                            whorlwind.write("secret", value)
                            Log.v("MainActivity", "wrote: " + value)
                        })

                whorlwind.read("secret")
                        .map { readResult -> readResult.value }
                        .subscribe({ value -> Toast.makeText(this, "can store securely: " + value, Toast.LENGTH_LONG).show() })

            } else {
                Toast.makeText(this, "can't store securely", Toast.LENGTH_LONG).show()
            }
        }

        Configurators(this).findConfigurableApps().forEach {
            Log.v(TAG, "packageName: ${it.activityInfo.packageName}");
        }

        // find applications that are configurable

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
