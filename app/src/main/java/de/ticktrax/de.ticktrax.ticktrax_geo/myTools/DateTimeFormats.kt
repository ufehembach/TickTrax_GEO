import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeFormats {
    fun formatDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = ZonedDateTime.now()
        val duration = Duration.between(zonedDateTime, now)
        val days = duration.toDays()
        return if (days == 0L) {
            // Today -> only time
            formatTime(timestamp)
        } else if (days == 1L) {
            // Yesterday -> yesterday + time
            "Yesterday " + formatTime(timestamp)
        } else if (days <= 7) {
            // Within a week -> date and time
            formatDateAndTime(timestamp)
        } else if (days <= 30) {
            // Older than a week, but within a month -> only week
            "Week of " + formatDate(timestamp)
        } else {
            // Older than a month -> only day
            formatDate(timestamp)
        }
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
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}