package com.opencallshield.net

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/** Resultado crudo de una peticion HTTP. */
data class HttpResult(val code: Int, val body: String) {
    val isSuccess: Boolean get() = code in 200..299
    fun json(): JSONObject = JSONObject(if (body.isBlank()) "{}" else body)
}

/**
 * Cliente HTTP minimo basado en HttpURLConnection (sin dependencias extra).
 * GitHub exige siempre la cabecera User-Agent.
 */
object Http {

    fun request(
        method: String,
        urlString: String,
        headers: Map<String, String> = emptyMap(),
        body: String? = null
    ): HttpResult {
        val conn = (URL(urlString).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 20_000
            readTimeout = 20_000
            setRequestProperty("User-Agent", "OpenCallShield")
            headers.forEach { (k, v) -> setRequestProperty(k, v) }
            if (body != null) doOutput = true
        }
        try {
            if (body != null) {
                OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(body) }
            }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream ?: conn.inputStream
            val text = stream?.let {
                BufferedReader(InputStreamReader(it, Charsets.UTF_8)).use { r -> r.readText() }
            } ?: ""
            return HttpResult(code, text)
        } finally {
            conn.disconnect()
        }
    }

    fun formBody(params: Map<String, String>): String =
        params.entries.joinToString("&") { (k, v) ->
            URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8")
        }
}
