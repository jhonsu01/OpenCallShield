package com.opencallshield.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Registro historico de una llamada bloqueada o silenciada por el motor de reglas.
 */
@Entity(tableName = "blocked_calls")
data class BlockedCall(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: String,
    val reason: String,
    val silenced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
