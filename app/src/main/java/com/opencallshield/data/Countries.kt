package com.opencallshield.data

/** Un pais con su bandera (emoji) y su prefijo telefonico internacional. */
data class Country(val name: String, val flag: String, val dialCode: String)

/**
 * Catalogo curado de paises frecuentemente asociados a llamadas SPAM internacionales,
 * mas los principales, para que el usuario bloquee por pais sin escribir prefijos.
 * El `dialCode` (con +) es lo que se guarda en la lista negra de prefijos.
 */
object Countries {
    val ALL: List<Country> = listOf(
        Country("Nigeria", "🇳🇬", "+234"),
        Country("India", "🇮🇳", "+91"),
        Country("Indonesia", "🇮🇩", "+62"),
        Country("Costa de Marfil", "🇨🇮", "+225"),
        Country("Pakistan", "🇵🇰", "+92"),
        Country("Filipinas", "🇵🇭", "+63"),
        Country("Bangladesh", "🇧🇩", "+880"),
        Country("Kenia", "🇰🇪", "+254"),
        Country("Marruecos", "🇲🇦", "+212"),
        Country("Egipto", "🇪🇬", "+20"),
        Country("Sudafrica", "🇿🇦", "+27"),
        Country("Rusia", "🇷🇺", "+7"),
        Country("China", "🇨🇳", "+86"),
        Country("Vietnam", "🇻🇳", "+84"),
        Country("Turquia", "🇹🇷", "+90"),
        Country("Rumania", "🇷🇴", "+40"),
        Country("Ucrania", "🇺🇦", "+380"),
        Country("Reino Unido", "🇬🇧", "+44"),
        Country("Estados Unidos/Canada", "🇺🇸", "+1"),
        Country("Mexico", "🇲🇽", "+52"),
        Country("Colombia", "🇨🇴", "+57"),
        Country("Venezuela", "🇻🇪", "+58"),
        Country("Peru", "🇵🇪", "+51"),
        Country("Ecuador", "🇪🇨", "+593"),
        Country("Chile", "🇨🇱", "+56"),
        Country("Argentina", "🇦🇷", "+54"),
        Country("Brasil", "🇧🇷", "+55"),
        Country("Bolivia", "🇧🇴", "+591"),
        Country("Panama", "🇵🇦", "+507"),
        Country("Guatemala", "🇬🇹", "+502"),
        Country("Republica Dominicana", "🇩🇴", "+1809"),
        Country("Espana", "🇪🇸", "+34"),
        Country("Francia", "🇫🇷", "+33"),
        Country("Alemania", "🇩🇪", "+49"),
        Country("Italia", "🇮🇹", "+39"),
        Country("Portugal", "🇵🇹", "+351"),
        Country("Albania", "🇦🇱", "+355"),
        Country("Kosovo", "🇽🇰", "+383"),
        Country("Emiratos Arabes", "🇦🇪", "+971"),
        Country("Arabia Saudita", "🇸🇦", "+966"),
        Country("Tailandia", "🇹🇭", "+66"),
        Country("Malasia", "🇲🇾", "+60"),
        Country("Nepal", "🇳🇵", "+977"),
        Country("Ghana", "🇬🇭", "+233"),
        Country("Camerun", "🇨🇲", "+237"),
        Country("Numeros premium (1900)", "⭐", "+1900")
    )

    /** Busca el pais cuyo dialCode coincide con el prefijo dado. */
    fun byDialCode(code: String): Country? = ALL.firstOrNull { it.dialCode == code }
}
