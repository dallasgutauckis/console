package extensions

import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest

fun PackageManager.getSignatureHash(packageName: String): String {
    val info = this.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    for (signature in info.signatures) {
        val md = MessageDigest.getInstance("SHA")
        md.update(signature.toByteArray())
        return String(Base64.encode(md.digest(), 0))
    }

    throw Exception("Unknown error, unable to find signature")
}