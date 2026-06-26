package com.opencallshield.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Almacenamiento cifrado del token de GitHub (EncryptedSharedPreferences).
 * Si el keystore del dispositivo fallara, degrada a prefs normales para no romper la app.
 */
class TokenStore(context: Context) {

    private val prefs: SharedPreferences = createPrefs(context.applicationContext)

    private fun createPrefs(appContext: Context): SharedPreferences = try {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            appContext,
            "ocs_secure",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        appContext.getSharedPreferences("ocs_secure_fallback", Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString("token", null)
        set(value) = prefs.edit().putString("token", value).apply()

    var login: String?
        get() = prefs.getString("login", null)
        set(value) = prefs.edit().putString("login", value).apply()

    /** Metodo con el que se autentico: "device" o "pat". */
    var method: String?
        get() = prefs.getString("method", null)
        set(value) = prefs.edit().putString("method", value).apply()

    val isLoggedIn: Boolean get() = !token.isNullOrBlank()

    fun save(token: String, login: String, method: String) {
        prefs.edit()
            .putString("token", token)
            .putString("login", login)
            .putString("method", method)
            .apply()
    }

    fun clear() = prefs.edit().clear().apply()
}
