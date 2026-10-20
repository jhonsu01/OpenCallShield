package com.opencallshield.engine

import android.content.Context
import com.opencallshield.data.SpamRepository

/**
 * Motor deterministico de reglas de deteccion de SPAM.
 *
 * Orden de evaluacion:
 *   1. Contacto conocido          -> PERMITIR (siempre gana)
 *   2. En base de datos de SPAM   -> BLOQUEAR
 *   3. Prefijo en lista negra     -> BLOQUEAR
 *   4. Numero desconocido/oculto  -> BLOQUEAR (solo si el usuario lo activo)
 *   5. En cualquier otro caso     -> PERMITIR
 */
class SpamDetector(
    private val context: Context,
    private val repo: SpamRepository
) {

    sealed class Decision {
        data object Allow : Decision()
        /** [reason] es un codigo (REASON_*), no una frase: la UI lo traduce al mostrarlo. */
        data class Block(val reason: String, val silence: Boolean) : Decision()
    }

    companion object {
        const val REASON_SPAM = "spam"
        const val REASON_HIDDEN = "hidden"
        const val REASON_UNKNOWN = "unknown"
        /** Se guarda como "prefix:+234". */
        const val REASON_PREFIX = "prefix:"
    }

    suspend fun evaluate(rawNumber: String?): Decision {
        val settings = repo.settings
        val silence = settings.silenceInsteadOfReject
        val number = SpamRepository.normalize(rawNumber.orEmpty())

        // 1. Contacto conocido -> nunca se bloquea
        if (number.isNotEmpty() && ContactsChecker.isKnownContact(context, number)) {
            return Decision.Allow
        }

        // 2. Reportado como SPAM (local o colaborativo)
        if (number.isNotEmpty() && repo.isSpam(number)) {
            return Decision.Block(REASON_SPAM, silence)
        }

        // 3. Prefijo en lista negra
        if (settings.blockPrefixes && number.isNotEmpty()) {
            val match = settings.prefixes().firstOrNull { number.startsWith(it) }
            if (match != null) {
                return Decision.Block(REASON_PREFIX + match, silence)
            }
        }

        // 4. Numero desconocido u oculto
        if (settings.blockUnknown) {
            return if (number.isEmpty()) {
                Decision.Block(REASON_HIDDEN, silence)
            } else {
                Decision.Block(REASON_UNKNOWN, silence)
            }
        }

        return Decision.Allow
    }
}
