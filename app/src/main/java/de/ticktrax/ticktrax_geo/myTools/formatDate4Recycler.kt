package de.ticktrax.ticktrax_geo.myTools

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun formatDate4Recycler(date: Date, context  : Context, locale: Locale = Locale.getDefault()): String {
    val now = System.currentTimeMillis()
    val timeZone = TimeZone.getDefault()
    val localCalendar = Calendar.getInstance(timeZone)

    // Set the time zone for the input date
    val inputCalendar = Calendar.getInstance()
    inputCalendar.time = date
    inputCalendar.timeZone = timeZone

    val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)
    val timeFormat = if (is24HourFormat) "HH:mm" else "hh:mm a"

    return when {
        DateUtils.isToday(date.time) -> {
            // Today: just time and day
            SimpleDateFormat("$timeFormat, EEEE", locale).format(date)
        }
        DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS) -> {
            // Yesterday: "yesterday" + time and day
            "Gestern, " + SimpleDateFormat("$timeFormat, EEEE", locale).format(date)
        }
        DateUtils.isToday(date.time + 2 * DateUtils.DAY_IN_MILLIS) || date.after(localCalendar.time) -> {
            // Day before yesterday and the full following week: "<weekday> + time"
            SimpleDateFormat("EEEE $timeFormat", locale).format(date)
        }
        else -> {
            // All other/older: date in ISO and then time in the local timezone
            SimpleDateFormat("yyyy-MM-dd'T'$timeFormat:ss", locale).format(date)
        }
    }
}