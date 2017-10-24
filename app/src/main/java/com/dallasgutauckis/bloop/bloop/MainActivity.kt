package com.dallasgutauckis.bloop.bloop

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotterknife.bindView


class MainActivity : AppCompatActivity() {

    val pinInput: TextView by bindView(R.id.pin_input)
    val saveButton: Button by bindView(R.id.save)
    val loadButton: Button by bindView(R.id.load)

    private var signature: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Whorlwind.create(this, SharedPreferencesStorage(this, "safespace"), )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
