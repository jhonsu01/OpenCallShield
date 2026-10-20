package com.opencallshield.ui

import android.content.Context
import androidx.annotation.StringRes

/**
 * Texto de interfaz como recurso + argumentos, en lugar de un String ya resuelto.
 *
 * El ViewModel no conoce el idioma elegido por el usuario (con el idioma por app de
 * Android, el contexto de la Application puede seguir en el idioma del sistema), asi
 * que los mensajes se resuelven en la UI con el contexto de la Activity. Un argumento
 * que sea a su vez [UiText] se resuelve de forma recursiva.
 */
data class UiText(@StringRes val resId: Int, val args: List<Any> = emptyList()) {
    fun resolve(context: Context): String {
        val resolved = args.map { if (it is UiText) it.resolve(context) else it }
        return context.getString(resId, *resolved.toTypedArray())
    }
}
