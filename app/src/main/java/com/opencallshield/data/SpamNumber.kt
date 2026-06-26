package com.opencallshield.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Numero marcado como SPAM. Puede provenir de un reporte local del usuario
 * (source = "local") o de la base colaborativa de GitHub (source = "github").
 */
@Entity(tableName = "spam_numbers")
data class SpamNumber(
    @PrimaryKey val number: String,
    val reports: Int = 1,
    val tag: String = "spam",
    val lastReported: Long = System.currentTimeMillis(),
    val source: String = "local"
)
