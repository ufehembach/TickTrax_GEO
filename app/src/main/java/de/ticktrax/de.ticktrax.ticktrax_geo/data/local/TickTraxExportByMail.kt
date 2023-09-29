package de.ticktrax.de.ticktrax.ticktrax_geo.data.local

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter
import java.io.IOException


class TickTraxExportByMail {
    fun sendEmailWithAttachment(context: Context, json: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Room Database Export")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Attached is the Room database data in JSON format."
        )

        // Save the JSON data to a file and attach it to the email
        val file = saveJsonToFile(context, json)
        val fileUri = FileProvider.getUriForFile(context, "com.your.package.fileprovider", file)
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun saveJsonToFile(context: Context, json: String): File {
        val file = File(context.getExternalFilesDir(null), "room_data.json")
        try {
            val writer = FileWriter(file)
            writer.write(json)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}