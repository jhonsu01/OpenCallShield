package com.opencallshield.sync

import com.opencallshield.data.SpamNumber
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Descarga la base colaborativa de numeros SPAM publicada como JSON en GitHub.
 *
 * Formato esperado:
 * {
 *   "version": "1.0",
 *   "updated_at": "2026-01-01",
 *   "numbers": [ { "number": "+1234567890", "reports": 120, "tag": "scam" } ]
 * }
 */
object GitHubSync {

    /** @throws Exception si la red falla o el JSON es invalido. */
    fun fetch(urlString: String): List<SpamNumber> {
        val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15_000
            readTimeout = 15_000
            requestMethod = "GET"
            setRequestProperty("Accept", "application/json")
        }

        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                throw IllegalStateException("HTTP $code al descargar la base")
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            return parse(body)
        } finally {
            connection.disconnect()
        }
    }

    private fun parse(body: String): List<SpamNumber> {
        val json = JSONObject(body)
        val array = json.optJSONArray("numbers") ?: return emptyList()
        val result = ArrayList<SpamNumber>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val number = obj.optString("number").trim()
            if (number.isEmpty()) continue
            result.add(
                SpamNumber(
                    number = number,
                    reports = obj.optInt("reports", 1),
                    tag = obj.optString("tag", "spam"),
                    source = "github"
                )
            )
        }
        return result
    }
}
