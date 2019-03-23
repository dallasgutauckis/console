package extensions

import android.view.View

fun View.padding(padding: Int) {
    setPadding(padding, padding, padding, padding)
}
