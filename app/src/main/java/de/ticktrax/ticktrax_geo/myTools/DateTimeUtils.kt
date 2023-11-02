package de.ticktrax.ticktrax_geo.myTools

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    private const val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"
    private const val UTC_TIMEZONE_ID = "UTC"
    fun formatDateTime(date: Date?): String {
        val sdf = SimpleDateFormat(DateTimeUtils.DEFAULT_PATTERN, Locale.getDefault())
        return sdf.format(date)
    }

    @Throws(ParseException::class)
    fun parseDateTime(dateString: String?): Date {
        val sdf = SimpleDateFormat(DateTimeUtils.DEFAULT_PATTERN, Locale.getDefault())
        return sdf.parse(dateString)
    } // Sie können auch weitere Methoden hinzufügen, um unterschiedliche Formate zu unterstützen

    fun formatDateTimeToUTC(date: Date): String {
        val sdf = SimpleDateFormat(DEFAULT_PATTERN+" z", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    @Throws(ParseException::class)
    fun parseDateTimeFromUTC(dateString: String): Date {
        val sdf = SimpleDateFormat(DEFAULT_PATTERN+" z", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(dateString) ?: throw ParseException("Invalid date format", 0)
    }
}

fun testit() {
// Ein Datum formatieren
    val currentDate = Date()
    val formattedDate = DateTimeUtils.formatDateTime(currentDate)
    logDebug("Formatted Date", formattedDate)

// Ein Datum analysieren
    try {
        val parsedDate = DateTimeUtils.parseDateTime("2023-09-29 15:30:00")
        logDebug("Parsed Date", parsedDate.toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }

}
