package com.opencallshield.data

import kotlinx.coroutines.flow.Flow

/**
 * Punto unico de acceso a la base local de SPAM y al historial de bloqueos.
 */
class SpamRepository(
    private val dao: SpamDao,
    val settings: SettingsStore
) {

    fun observeAll(): Flow<List<SpamNumber>> = dao.observeAll()

    fun observeBlocked(): Flow<List<BlockedCall>> = dao.observeBlocked()

    suspend fun count(): Int = dao.count()

    suspend fun isSpam(number: String): Boolean =
        dao.findByNumber(normalize(number)) != null

    /** Reporta un numero como SPAM. Si ya existe, incrementa el contador. */
    suspend fun report(number: String, tag: String = "spam") {
        val n = normalize(number)
        if (n.isEmpty()) return
        val existing = dao.findByNumber(n)
        val updated = existing?.copy(
            reports = existing.reports + 1,
            tag = tag,
            lastReported = System.currentTimeMillis()
        ) ?: SpamNumber(number = n, reports = 1, tag = tag, source = "local")
        dao.upsert(updated)
    }

    suspend fun remove(number: String) = dao.deleteByNumber(normalize(number))

    /**
     * Mezcla la base publica con la local. Si un numero que el usuario ya tenia
     * reportado (source "local") tambien aparece en la base publica, se marca como
     * "github" para NO volver a aportarlo: asi solo se reportan numeros nuevos y se
     * evitan Issues con duplicados.
     */
    suspend fun mergeRemote(remote: List<SpamNumber>) {
        if (remote.isEmpty()) return
        for (item in remote) {
            val n = normalize(item.number)
            if (n.isEmpty()) continue
            val existing = dao.findByNumber(n)
            if (existing == null) {
                dao.upsert(item.copy(number = n, source = "github"))
            } else if (existing.source != "github") {
                dao.upsert(existing.copy(source = "github"))
            }
        }
    }

    suspend fun logBlocked(number: String, reason: String, silenced: Boolean) {
        dao.insertBlocked(
            BlockedCall(number = number, reason = reason, silenced = silenced)
        )
    }

    suspend fun clearHistory() = dao.clearBlocked()

    companion object {
        /** Normaliza un numero: elimina espacios, guiones y parentesis. */
        fun normalize(raw: String): String =
            raw.filter { it.isDigit() || it == '+' }
    }
}
