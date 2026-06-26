package com.opencallshield.service

import android.telecom.Call
import android.telecom.CallScreeningService
import com.opencallshield.data.AppDatabase
import com.opencallshield.data.SettingsStore
import com.opencallshield.data.SpamRepository
import com.opencallshield.engine.SpamDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Servicio del sistema que intercepta cada llamada entrante (Android 10+).
 *
 * El sistema enlaza este servicio cuando la app posee el rol
 * ROLE_CALL_SCREENING. Para cada llamada se evaluan las reglas del
 * [SpamDetector] y se responde permitiendo, silenciando o rechazando.
 */
class SpamCallScreeningService : CallScreeningService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        // Solo evaluamos llamadas entrantes.
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        val number = callDetails.handle?.schemeSpecificPart

        scope.launch {
            val repo = SpamRepository(
                AppDatabase.get(applicationContext).spamDao(),
                SettingsStore(applicationContext)
            )
            val decision = SpamDetector(applicationContext, repo).evaluate(number)
            val response = buildResponse(decision)

            if (decision is SpamDetector.Decision.Block) {
                repo.logBlocked(
                    number = number ?: "(oculto)",
                    reason = decision.reason,
                    silenced = decision.silence
                )
            }

            respondToCall(callDetails, response)
        }
    }

    private fun buildResponse(decision: SpamDetector.Decision): CallResponse {
        val builder = CallResponse.Builder()
        when (decision) {
            is SpamDetector.Decision.Allow -> {
                // Todos los flags en false = la llamada continua normal.
            }
            is SpamDetector.Decision.Block -> {
                if (decision.silence) {
                    // Silenciar: el telefono no suena, pero queda como perdida.
                    builder.setSilenceCall(true)
                } else {
                    // Rechazar: se corta sin molestar al usuario.
                    builder.setDisallowCall(true)
                    builder.setRejectCall(true)
                    builder.setSkipCallLog(false)
                    builder.setSkipNotification(true)
                }
            }
        }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
