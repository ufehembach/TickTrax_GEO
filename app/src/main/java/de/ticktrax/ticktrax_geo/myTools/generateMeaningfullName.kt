package de.ticktrax.ticktrax_geo.myTools

fun generateMeaningfulName(name: String, displayName: String): String {
    return if (!name.isNullOrBlank()) {
        // Wenn der Displayname nicht leer ist, verwenden Sie ihn
        name
    } else {
        // Wenn der Displayname leer ist, nehmen Sie die ersten vier Wörter aus dem Namen
        val words = displayName.split(" ")
        words.take(4).joinToString(" ")
    }
}