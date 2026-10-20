package com.opencallshield.auth

import com.opencallshield.net.Http

/**
 * Autenticacion con GitHub para una app movil sin backend.
 *
 * - Device Flow: oficial para apps/CLIs; solo usa el Client ID publico (sin secreto).
 * - PAT: el usuario pega un Personal Access Token con scope `public_repo`.
 */
class GitHubAuthManager {

    data class DeviceCode(
        val deviceCode: String,
        val userCode: String,
        val verificationUri: String,
        val interval: Int,
        val expiresIn: Int
    )

    sealed class PollResult {
        data class Success(val token: String) : PollResult()
        data object Pending : PollResult()
        data class SlowDown(val interval: Int) : PollResult()
        /** [code] es una de las constantes ERROR_*; la UI la traduce al idioma activo. */
        data class Error(val code: String, val detail: String? = null) : PollResult()
    }

    /** El token no es valido o no tiene permisos; la UI muestra el mensaje traducido. */
    class InvalidTokenException(val httpCode: Int) : Exception("Invalid token ($httpCode)")

    /** Paso 1 del Device Flow: solicita el codigo de dispositivo. */
    fun startDeviceFlow(clientId: String, scope: String = SCOPE): DeviceCode {
        val res = Http.request(
            method = "POST",
            urlString = "https://github.com/login/device/code",
            headers = mapOf(
                "Accept" to "application/json",
                "Content-Type" to "application/x-www-form-urlencoded"
            ),
            body = Http.formBody(mapOf("client_id" to clientId, "scope" to scope))
        )
        if (!res.isSuccess) throw IllegalStateException("Error ${res.code}: ${res.body}")
        val j = res.json()
        if (j.has("error")) {
            throw IllegalStateException(j.optString("error_description", j.getString("error")))
        }
        return DeviceCode(
            deviceCode = j.getString("device_code"),
            userCode = j.getString("user_code"),
            verificationUri = j.getString("verification_uri"),
            interval = j.optInt("interval", 5),
            expiresIn = j.optInt("expires_in", 900)
        )
    }

    /** Paso 2 del Device Flow: consulta si el usuario ya autorizo. */
    fun pollForToken(clientId: String, deviceCode: String): PollResult {
        val res = Http.request(
            method = "POST",
            urlString = "https://github.com/login/oauth/access_token",
            headers = mapOf(
                "Accept" to "application/json",
                "Content-Type" to "application/x-www-form-urlencoded"
            ),
            body = Http.formBody(
                mapOf(
                    "client_id" to clientId,
                    "device_code" to deviceCode,
                    "grant_type" to "urn:ietf:params:oauth:grant-type:device_code"
                )
            )
        )
        val j = res.json()
        if (j.has("access_token")) return PollResult.Success(j.getString("access_token"))
        return when (j.optString("error")) {
            "authorization_pending" -> PollResult.Pending
            "slow_down" -> PollResult.SlowDown(j.optInt("interval", 5))
            "expired_token" -> PollResult.Error(ERROR_EXPIRED)
            "access_denied" -> PollResult.Error(ERROR_DENIED)
            "" -> PollResult.Error(ERROR_UNEXPECTED, res.code.toString())
            else -> PollResult.Error(
                ERROR_OTHER, j.optString("error_description", j.getString("error"))
            )
        }
    }

    /** Valida un token (PAT o device) y devuelve el login del usuario. */
    fun fetchLogin(token: String): String {
        val res = Http.request(
            method = "GET",
            urlString = "https://api.github.com/user",
            headers = mapOf(
                "Accept" to "application/vnd.github+json",
                "Authorization" to "Bearer $token"
            )
        )
        if (!res.isSuccess) throw InvalidTokenException(res.code)
        return res.json().getString("login")
    }

    companion object {
        const val SCOPE = "public_repo"
        const val ERROR_EXPIRED = "expired"
        const val ERROR_DENIED = "denied"
        const val ERROR_UNEXPECTED = "unexpected"
        const val ERROR_OTHER = "other"
    }
}
