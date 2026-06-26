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
        data class Block(val reason: String, val silence: Boolean) : Decision()
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
            return Decision.Block("Reportado como SPAM", silence)
        }

        // 3. Prefijo en lista negra
        if (settings.blockPrefixes && number.isNotEmpty()) {
            val match = settings.prefixes().firstOrNull { number.startsWith(it) }
            if (match != null) {
                return Decision.Block("Prefijo sospechoso ($match)", silence)
            }
        }

        // 4. Numero desconocido u oculto
        if (settings.blockUnknown) {
            return if (number.isEmpty()) {
                Decision.Block("Numero oculto", silence)
            } else {
                Decision.Block("Numero fuera de contactos", silence)
            }
        }

        return Decision.Allow
    }
}
