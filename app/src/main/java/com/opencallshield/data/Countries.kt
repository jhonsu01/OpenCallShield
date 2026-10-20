package com.opencallshield.data

import java.util.Locale

/**
 * Un pais con su bandera (emoji) y su prefijo telefonico internacional.
 *
 * [isoCodes] son codigos ISO 3166 separados por coma ("US,CA" para el prefijo +1
 * compartido). El nombre visible NO se guarda: se obtiene de [Locale] en el idioma
 * activo de la app, asi los 46 paises quedan traducidos a cualquier idioma sin
 * mantener tablas a mano. Vacio = entrada especial sin pais (numeros premium).
 */
data class Country(val isoCodes: String, val flag: String, val dialCode: String) {

    /** Nombre del pais en [locale], o null si es una entrada especial sin pais. */
    fun displayName(locale: Locale): String? {
        if (isoCodes.isEmpty()) return null
        return isoCodes.split(",").joinToString(" / ") {
            Locale.Builder().setRegion(it).build().getDisplayCountry(locale)
        }
    }
}

/**
 * Catalogo curado de paises frecuentemente asociados a llamadas SPAM internacionales,
 * mas los principales, para que el usuario bloquee por pais sin escribir prefijos.
 * El `dialCode` (con +) es lo que se guarda en la lista negra de prefijos.
 */
object Countries {
    val ALL: List<Country> = listOf(
        Country("NG", "🇳🇬", "+234"),
        Country("IN", "🇮🇳", "+91"),
        Country("ID", "🇮🇩", "+62"),
        Country("CI", "🇨🇮", "+225"),
        Country("PK", "🇵🇰", "+92"),
        Country("PH", "🇵🇭", "+63"),
        Country("BD", "🇧🇩", "+880"),
        Country("KE", "🇰🇪", "+254"),
        Country("MA", "🇲🇦", "+212"),
        Country("EG", "🇪🇬", "+20"),
        Country("ZA", "🇿🇦", "+27"),
        Country("RU", "🇷🇺", "+7"),
        Country("CN", "🇨🇳", "+86"),
        Country("VN", "🇻🇳", "+84"),
        Country("TR", "🇹🇷", "+90"),
        Country("RO", "🇷🇴", "+40"),
        Country("UA", "🇺🇦", "+380"),
        Country("GB", "🇬🇧", "+44"),
        Country("US,CA", "🇺🇸", "+1"),
        Country("MX", "🇲🇽", "+52"),
        Country("CO", "🇨🇴", "+57"),
        Country("VE", "🇻🇪", "+58"),
        Country("PE", "🇵🇪", "+51"),
        Country("EC", "🇪🇨", "+593"),
        Country("CL", "🇨🇱", "+56"),
        Country("AR", "🇦🇷", "+54"),
        Country("BR", "🇧🇷", "+55"),
        Country("BO", "🇧🇴", "+591"),
        Country("PA", "🇵🇦", "+507"),
        Country("GT", "🇬🇹", "+502"),
        Country("DO", "🇩🇴", "+1809"),
        Country("ES", "🇪🇸", "+34"),
        Country("FR", "🇫🇷", "+33"),
        Country("DE", "🇩🇪", "+49"),
        Country("IT", "🇮🇹", "+39"),
        Country("PT", "🇵🇹", "+351"),
        Country("AL", "🇦🇱", "+355"),
        Country("XK", "🇽🇰", "+383"),
        Country("AE", "🇦🇪", "+971"),
        Country("SA", "🇸🇦", "+966"),
        Country("TH", "🇹🇭", "+66"),
        Country("MY", "🇲🇾", "+60"),
        Country("NP", "🇳🇵", "+977"),
        Country("GH", "🇬🇭", "+233"),
        Country("CM", "🇨🇲", "+237"),
        Country("", "⭐", "+1900")
    )

    /** Busca el pais cuyo dialCode coincide con el prefijo dado. */
    fun byDialCode(code: String): Country? = ALL.firstOrNull { it.dialCode == code }
}
