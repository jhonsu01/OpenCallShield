package com.opencallshield.data

import android.content.Context

/**
 * Preferencias del usuario sobre el comportamiento del motor de bloqueo.
 * Respaldado por SharedPreferences (no requiere migracion de esquema).
 */
class SettingsStore(context: Context) {

    private val prefs =
        context.applicationContext.getSharedPreferences("ocs_settings", Context.MODE_PRIVATE)

    /** Bloquear numeros que no estan en la agenda de contactos. */
    var blockUnknown: Boolean
        get() = prefs.getBoolean(KEY_BLOCK_UNKNOWN, false)
        set(value) = prefs.edit().putBoolean(KEY_BLOCK_UNKNOWN, value).apply()

    /** Bloquear numeros cuyo prefijo este en la lista negra. */
    var blockPrefixes: Boolean
        get() = prefs.getBoolean(KEY_BLOCK_PREFIXES, true)
        set(value) = prefs.edit().putBoolean(KEY_BLOCK_PREFIXES, value).apply()

    /** Si true, las llamadas se silencian en vez de rechazarse. */
    var silenceInsteadOfReject: Boolean
        get() = prefs.getBoolean(KEY_SILENCE, false)
        set(value) = prefs.edit().putBoolean(KEY_SILENCE, value).apply()

    /** Prefijos en lista negra, separados por coma. */
    var prefixList: String
        get() = prefs.getString(KEY_PREFIXES, DEFAULT_PREFIXES) ?: DEFAULT_PREFIXES
        set(value) = prefs.edit().putString(KEY_PREFIXES, value).apply()

    /** URL del JSON publico colaborativo en GitHub. */
    var syncUrl: String
        get() = prefs.getString(KEY_SYNC_URL, DEFAULT_SYNC_URL) ?: DEFAULT_SYNC_URL
        set(value) = prefs.edit().putString(KEY_SYNC_URL, value).apply()

    /** Client ID de la OAuth App de GitHub (publico). Necesario para el Device Flow. */
    var githubClientId: String
        get() = prefs.getString(KEY_CLIENT_ID, DEFAULT_CLIENT_ID) ?: DEFAULT_CLIENT_ID
        set(value) = prefs.edit().putString(KEY_CLIENT_ID, value).apply()

    /** Owner del repo destino de los aportes (Issues). */
    var contribOwner: String
        get() = prefs.getString(KEY_CONTRIB_OWNER, DEFAULT_CONTRIB_OWNER) ?: DEFAULT_CONTRIB_OWNER
        set(value) = prefs.edit().putString(KEY_CONTRIB_OWNER, value).apply()

    /** Nombre del repo destino de los aportes (Issues). */
    var contribRepo: String
        get() = prefs.getString(KEY_CONTRIB_REPO, DEFAULT_CONTRIB_REPO) ?: DEFAULT_CONTRIB_REPO
        set(value) = prefs.edit().putString(KEY_CONTRIB_REPO, value).apply()

    fun prefixes(): List<String> =
        prefixList.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    companion object {
        private const val KEY_BLOCK_UNKNOWN = "block_unknown"
        private const val KEY_BLOCK_PREFIXES = "block_prefixes"
        private const val KEY_SILENCE = "silence_instead_reject"
        private const val KEY_PREFIXES = "prefix_list"
        private const val KEY_SYNC_URL = "sync_url"
        private const val KEY_CLIENT_ID = "github_client_id"
        private const val KEY_CONTRIB_OWNER = "contrib_owner"
        private const val KEY_CONTRIB_REPO = "contrib_repo"

        // Prefijos de ejemplo frecuentemente asociados a fraudes internacionales.
        const val DEFAULT_PREFIXES = "+234,+91,+62,+1900,+225"
        const val DEFAULT_SYNC_URL =
            "https://raw.githubusercontent.com/jhonsu01/OpenCallShield/main/spam_numbers.json"

        // Client ID de la OAuth App (publico). Rellenar tras crear la OAuth App en GitHub.
        // Tambien se puede configurar desde la pantalla Cuenta de la app.
        const val DEFAULT_CLIENT_ID = ""
        const val DEFAULT_CONTRIB_OWNER = "jhonsu01"
        const val DEFAULT_CONTRIB_REPO = "OpenCallShield"
    }
}
