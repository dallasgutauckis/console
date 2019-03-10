package com.dallasgutauckis.bloop.consoleimplementationsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.dallasgutauckis.bloop.consoleimplementationsample.debug.ImplApplication

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        (findViewById<TextView>(R.id.configurable_text) as TextView).text = (application as ImplApplication).lastValue
    }
}
