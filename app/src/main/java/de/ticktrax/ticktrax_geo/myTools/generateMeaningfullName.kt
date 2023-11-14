package de.ticktrax.ticktrax_geo.myTools

class generateMeaningfullName {
    fun generateMeaningfulName(name: String, displayName: String?): String {
        return if (!displayName.isNullOrBlank()) {
            // Wenn der Displayname nicht leer ist, verwenden Sie ihn
            displayName
        } else {
            // Wenn der Displayname leer ist, nehmen Sie die ersten vier Wörter aus dem Namen
            val words = name.split(" ")
            words.take(4).joinToString(" ")
        }
    }
}