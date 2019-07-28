package com.dallasgutauckis.henson.consoleimplementationsample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dallasgutauckis.henson.consoleimplementationsample.debug.ImplApplication

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        (findViewById<TextView>(R.id.configurable_text)).text = (application as ImplApplication).lastValue
    }
}
