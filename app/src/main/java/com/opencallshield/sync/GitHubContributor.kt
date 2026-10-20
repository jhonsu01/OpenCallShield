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
        require(numbers.isNotEmpty()) { "No numbers to contribute." }

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
            // El Issue lo procesa el mantenedor (y .github/scripts/integrate_spam.py, que
            // solo lee el bloque ```json```), asi que el texto va en ingles, idioma del repo.
            append("Numbers proposed for `spam_numbers.json`, sent from the OpenCallShield app.\n\n")
            append("Count: ${numbers.size}\n\n")
            append("```json\n")
            append(jsonArray.toString(2))
            append("\n```\n")
        }

        // Sin labels: asignar/crear etiquetas requiere permiso de escritura sobre el
        // repo. Un usuario externo (no colaborador) puede abrir un Issue, pero no
        // etiquetarlo; incluir labels provocaba un 403. El titulo ya identifica el aporte.
        val payload = JSONObject()
            .put("title", "SPAM database contribution: ${numbers.size} number(s)")
            .put("body", body)

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
