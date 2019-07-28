package com.dallasgutauckis.henson.muppet

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import com.dallasgutauckis.henson.muppet.logger.Logger
import com.dallasgutauckis.configurator.shared.MessageResponseCode
import com.dallasgutauckis.configurator.shared.Signing
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

/**
 * use autoConfigure, or manually add signatures via addSignature
 */
object Configurator {
    private val TAG = "Configurator"

    private val signatures: MutableList<String> = ArrayList()

    var logger: Logger? = null

    var configListener: ConfigListener? = null

    fun addSignature(signature: String) {
        signatures.add(signature)
        logger?.v(TAG, "Added signature: $signature")
    }

    fun autoConfigure(context: Context) {
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val configuratorUrl = checkNotNull(applicationInfo.metaData.getString("configurator_url")) {
            "configurator_url value is missing from AndroidManifest.xml\n" +
                    "Either add configurator_url as <meta-data /> to the <application /> element, or\n" +
                    "manually configure Configurator using Configurator.addSignature(publicKey)"
        }

        // TODO abstract this so the user can provide her own HTTP request implementation
        val response = OkHttpClient().newCall(Request.Builder().url(configuratorUrl).build())
                .execute()

        if (response.code() == 200) {
            response.body()?.string()?.let {
                val jsonArray = JSONArray(it)
                var i = 0
                val length = jsonArray.length()

                while (i < length) {
                    addSignature(jsonArray.getString(i++))
                }
            }

            // Expecting JSON array of Strings
        } else {
            TODO("retry?")
        }
        // fetch signatures from configurator_url
    }

    fun onMessage(publicKey: ByteArray, jsonPayload: ByteArray, signature: ByteArray): Int {
        signatures.forEach {
            log("onMessage, current sig: $it")
        }

        val publicKeyString = Base64.encode(publicKey, Base64.NO_WRAP).toString(charset("utf-8"))
        val signatureBase65 = Base64.encode(signature, Base64.NO_WRAP).toString(charset("utf-8"))

        log("Got message: ${jsonPayload.toString(charset("utf-8"))}; " +
                "signed: $signatureBase65; " +
                "from publick key: $publicKeyString")

        if (!signatures.contains(publicKeyString)) {
            return MessageResponseCode.ERROR_NO_MATCHING_PUBLIC_KEY
        }

        log("Valid sender!")

        if (!Signing.verifySignature(Signing.SignedData(jsonPayload, signature, publicKey))) {
            return MessageResponseCode.ERROR_INCORRECTLY_SIGNED_PAYLOAD
        }

        log("And it's signed properly!")

        // propagate out to the app from here
        configListener?.onConfigUpdated("example", jsonPayload.toString(charset("utf-8")))

        return MessageResponseCode.SUCCESS
    }

    private fun log(message: String) {
        logger?.v(TAG, message)
    }

    interface ConfigListener {
        fun onConfigUpdated(key: String, value: String)
    }
}