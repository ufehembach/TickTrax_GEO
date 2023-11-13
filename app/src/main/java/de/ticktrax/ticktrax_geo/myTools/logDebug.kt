package de.ticktrax.ticktrax_geo.myTools

import android.util.Log

fun logDebug(tag: String, message: String) {
    val stackTraceElement = Thread.currentThread().stackTrace[3] // Index 3, da wir die Aufrufer-Methode erhalten möchten

    val fileName = stackTraceElement.fileName
    val lineNumber = stackTraceElement.lineNumber

    val logMessage = "$fileName $lineNumber: $message"
    Log.d(tag, logMessage)
}
fun logError(tag: String, message: String) {
    val stackTraceElement = Thread.currentThread().stackTrace[3] // Index 3, da wir die Aufrufer-Methode erhalten möchten

    val fileName = stackTraceElement.fileName
    val lineNumber = stackTraceElement.lineNumber

    val logMessage = "$fileName $lineNumber: $message"
    Log.e(tag, logMessage)
}
