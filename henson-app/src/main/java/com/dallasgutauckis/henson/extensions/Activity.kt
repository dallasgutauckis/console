package com.dallasgutauckis.henson.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}