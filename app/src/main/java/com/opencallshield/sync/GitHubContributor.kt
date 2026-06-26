package com.opencallshield.sync

import com.opencallshield.data.SpamNumber
import com.opencallshield.net.Http
import org.json.JSONArray
import org.json.JSONObject

/**
 * Envia aportes a la base publica abriendo un Issue en el repo destino con la
 * lista de numeros propuestos en formato JSON, listo para integrar en
 * `spam_numbers.json`. No requiere permiso de escritura sobre el repo: cualquier
 * usuario autenticado puede abrir un Issue en un repo publico.
 */
object GitHubContributor {

    /** Crea el Issue con la propuesta y devuelve su URL (html_url). */
    fun proposeNumbers(
        token: String,
        owner: String,
        repo: String,
        numbers: List<SpamNumber>
    ): String {
        require(numbers.isNotEmpty()) { "No hay numeros para aportar." }

        val jsonArray = JSONArray()
        numbers.forEach { n ->
            jsonArray.put(
                JSONObject()
                    .put("number", n.number)
                    .put("reports", n.reports)
                    .put("tag", n.tag)
            )
        }

        val body = buildString {
            append("Propuesta de numeros para `spam_numbers.json`, enviada desde la app OpenCallShield.\n\n")
            append("Cantidad: ${numbers.size}\n\n")
            append("```json\n")
            append(jsonArray.toString(2))
            append("\n```\n")
        }

        val payload = JSONObject()
            .put("title", "Aporte a base SPAM: ${numbers.size} numero(s)")
            .put("body", body)
            .put("labels", JSONArray().put("spam-db"))

        val res = Http.request(
            method = "POST",
            urlString = "https://api.github.com/repos/$owner/$repo/issues",
            headers = mapOf(
                "Accept" to "application/vnd.github+json",
                "Authorization" to "Bearer $token",
                "Content-Type" to "application/json"
            ),
            body = payload.toString()
        )
        if (!res.isSuccess) throw IllegalStateException("Error ${res.code}: ${res.body.take(200)}")
        return res.json().optString("html_url", "Issue creado")
    }
}
