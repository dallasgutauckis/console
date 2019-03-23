package com.dallasgutauckis.henson

import android.content.Intent

/**
 * Created by dallas on 2017-10-28.
 */
class ConfigureActionIntentBuilder {
    val INTENT_ACTION_ACTION = "configurator.intent.ACTION"

    fun create(packageName: String): Intent {
        val intent = Intent(INTENT_ACTION_ACTION)
        intent.`package` = packageName
        return intent
    }
}