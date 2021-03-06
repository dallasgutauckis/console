package com.dallasgutauckis.configurator.shared;

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.*
import java.security.spec.X509EncodedKeySpec

/**
 * Originally from "cketti" on Android Study Group Slack via https://gist.github.com/cketti/fb3489275bf40e2728495898b7526934
 *
 * Refusal for licensing/attribution: https://androidstudygroup.slack.com/archives/C03KMK1CL/p1509326838000014
 *
 * Modified to suit the needs of this project
 */
object Signing {

    private fun getAlias(packageName: String): String {
        return "app/${packageName}"
    }

    fun generateKeyPair(packageName: String): PublicKey {
        val alias = getAlias(packageName)
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_SIGN.or(KeyProperties.PURPOSE_VERIFY))
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .build()
            keyPairGenerator.initialize(keyGenParameterSpec)
        } else {
            keyPairGenerator.initialize(65535)
        }

        return keyPairGenerator.generateKeyPair().public
    }

    fun hasKeyPair(packageName: String): Boolean {
        val alias = getAlias(packageName)
        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        return ks.containsAlias(alias)
    }

    fun removeKeyPair(packageName: String) {
        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        ks.deleteEntry(getAlias(packageName))
    }

    fun signData(packageName: String, data: ByteArray): SignedData {
        val alias = getAlias(packageName)
        val entry = getAliasEntry(alias)

        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(entry.privateKey)
        signature.update(data)
        val signatureBytes = signature.sign()

        val encodedPublicKey = entry.certificate.publicKey.encoded
        return SignedData(data, signatureBytes, encodedPublicKey)
    }

    fun verifySignature(signedData: SignedData): Boolean {
        val keyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC)
        val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(signedData.encodedPublicKey))

        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(signedData.data)
        return signature.verify(signedData.signature)
    }

    private fun getAliasEntry(alias: String): KeyStore.PrivateKeyEntry {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val entry = keyStore.getEntry(alias, null)
        Log.v("DALLAS", "alias: $alias, entry: $entry")
        return entry as? KeyStore.PrivateKeyEntry ?: throw IllegalStateException("Not an instance of a PrivateKeyEntry")
    }

    fun getPublicKey(packageName: String): PublicKey {
        val alias = getAlias(packageName)
        return getAliasEntry(alias).certificate.publicKey
    }

    data class SignedData(
            val data: ByteArray,
            val signature: ByteArray,
            val encodedPublicKey: ByteArray)
}