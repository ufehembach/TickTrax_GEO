package de.ticktrax.ticktrax_geo.myTools

import java.security.MessageDigest

fun createUniqueKey(value1: Double, value2: Double): String {
    val combinedString = "$value1|$value2"

    // Erstellen Sie einen SHA-256-Hash-Code für die kombinierte Zeichenkette
    val bytes = MessageDigest.getInstance("SHA-256").digest(combinedString.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
