import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateTimeFormats {
    fun formatDateTime(myDate: Date): String {
        val nowDate = Date()

        val nowCalendar = Calendar.getInstance()
        nowCalendar.time = nowDate
        val myCalendar = Calendar.getInstance()
        myCalendar.time = myDate

        val diffTimestampMillis = nowDate.time - myDate.time
        val diffDays = TimeUnit.DAYS.convert(diffTimestampMillis, TimeUnit.MILLISECONDS)


        return if (diffDays == 0L) {
            // Today -> only time
            formatTime(myDate.time)
        } else if (diffDays == 1L) {
            // Yesterday -> yesterday + time
            "Yesterday " + formatTime(myDate.time)
        } else if (diffDays <= 7) {
            // Within a week -> date and time
            formatDateAndTime(myDate.time)
        } else if (diffDays <= 30) {
            // Older than a week, but within a month -> only week
            "Week of " + formatDate(myDate.time)
        } else {
            // Older than a month -> only day
            formatDate(myDate.time)
        }
        return "nix"
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatDateAndTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}