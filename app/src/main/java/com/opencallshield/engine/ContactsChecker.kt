package com.opencallshield.engine

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

/**
 * Comprueba si un numero pertenece a un contacto guardado.
 * Devuelve false de forma segura si no hay permiso de contactos.
 */
object ContactsChecker {

    fun isKnownContact(context: Context, number: String): Boolean {
        if (number.isBlank()) return false
        return try {
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )
            context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup._ID),
                null,
                null,
                null
            )?.use { cursor -> cursor.count > 0 } ?: false
        } catch (e: SecurityException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}
