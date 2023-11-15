package de.ticktrax.ticktrax_geo.myTools

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import jxl.Workbook
import jxl.write.Label
import java.io.*
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import de.ticktrax.ticktrax_geo.data.datamodels.ExportOrder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ExportViaMail<T : Any>(private val data: List<T>, val theContext: Context) {
    fun exportAll(filePath: File?): Intent? {
        val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            var jsonFile = File(filePath, "TT-$currentDate-${getFirstEmailAddress()}.json")
            // Export as JSON
            exportAsJson(data, jsonFile)

            // Export as XLS
            var xlsFile = File(filePath, "TT-$currentDate-${getFirstEmailAddress()}.xls")
            exportAsXls(data, xlsFile)

            // Export as CSV
            var csvFile = File(filePath, "TT-$currentDate-${getFirstEmailAddress()}.csv")
            exportAsCsv(data, csvFile)

            // Erstellen Sie eine Liste von Dateien, die angehängt werden sollen
            val filesToAttach = ArrayList<Uri>()
            filesToAttach.add(getUriFromFile(jsonFile))
            filesToAttach.add(getUriFromFile(xlsFile))
            filesToAttach.add(getUriFromFile(csvFile))

            // Erstellen Sie den E-Mail-Intent
            emailIntent.type = "text/plain"
            //   emailIntent.type = "application/json"

            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getFirstEmailAddress()))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dateien exportiert am $currentDate")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Anbei finden Sie die exportierten Dateien.")

            // Fügen Sie die Dateien als Anhänge hinzu
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesToAttach)

            // Starten Sie die E-Mail-Anwendung
            // theContext.startActivity(Intent.createChooser(emailIntent, "E-Mail senden"))
        } catch (e: Exception) {
            logError("EmailSender", "Fehler beim Versenden der E-Mail" + e.toString())
        }
        return (
                if (emailIntent != null) {
                    // Erfolgsfall
                    emailIntent
                } else {
                    null
                })
    }

    private fun exportAsJson(data: List<T>, fullFile: File) {
        val jsonContent = Gson().toJson(data)
        saveToFile(jsonContent, fullFile.canonicalPath, "json")
    }

    private fun saveToFile(content: String, fullFileName: String, extension: String) {
        val file = File(fullFileName)
        BufferedWriter(FileWriter(file)).use { writer ->
            writer.write(content)
        }
    }

    private fun exportAsXls(data: List<T>, fullFile: File) {
        val workbook = Workbook.createWorkbook(FileOutputStream(fullFile))
        val sheet = workbook.createSheet("TT-Data", 0)

        // Write headers
        data.firstOrNull()?.let { entity ->
            entity::class.java.declaredFields
                .filter { it.isAnnotationPresent(ExportOrder::class.java) }
                .sortedBy { it.getAnnotation(ExportOrder::class.java).order }
                .forEachIndexed { colIndex, field ->
                    sheet.addCell(Label(colIndex, 0, field.name))
                }
        }

        data.forEachIndexed { rowIndex, entity ->
            entity::class.java.declaredFields
                .filter { it.isAnnotationPresent(ExportOrder::class.java) }
                .sortedBy { it.getAnnotation(ExportOrder::class.java).order }
                .forEachIndexed { colIndex, field ->
                    field.isAccessible = true
                    val cellValue = field.get(entity)?.toString() ?: ""
                    sheet.addCell(Label(colIndex, rowIndex + 1, cellValue))
                }
        }

        workbook.write()
        workbook.close()
    }

    private fun exportAsCsvSorted(data: List<T>, fullFile: File) {
        BufferedWriter(FileWriter(fullFile)).use { writer ->
            // Write headers
            data.firstOrNull()?.let { entity ->
                val headers = entity::class.java.declaredFields.map { it.name }
                writer.write(headers.joinToString(",") + "\n")
            }

            // Write data
            data.forEach { entity ->
                val rowData = entity::class.java.declaredFields.joinToString(",") { field ->
                    field.isAccessible = true
                    field.get(entity)?.toString() ?: ""
                }
                writer.write("$rowData\n")
            }
        }
    }

    private fun <T : Any> exportAsCsv(data: List<T>, fullFile: File) {
        BufferedWriter(FileWriter(fullFile)).use { writer ->
            // Write headers
            data.firstOrNull()?.let { entity ->
                val headers = entity::class.java.declaredFields
                    .filter { it.isAnnotationPresent(ExportOrder::class.java) }
                    .sortedBy { it.getAnnotation(ExportOrder::class.java).order }
                    .joinToString(",") { it.name }
                writer.write("$headers\n")
            }

            // Write data
            data.forEach { entity ->
                val rowData = entity::class.java.declaredFields
                    .filter { it.isAnnotationPresent(ExportOrder::class.java) }
                    .sortedBy { it.getAnnotation(ExportOrder::class.java).order }
                    .joinToString(",") { field ->
                        field.isAccessible = true
                        field.get(entity)?.toString() ?: ""
                    }
                writer.write("$rowData\n")
            }
        }
    }

    private fun getUriFromFile(file: File): Uri {
        return FileProvider.getUriForFile(
            theContext,
            theContext.packageName + ".fileprovider",
            file
        )
    }

    private fun getFirstEmailAddress(): String? {
        // Hier sollte die Logik implementiert werden, um die erste E-Mail-Adresse auf dem Gerät zu erhalten.
        // Du kannst die Android AccountManager-API verwenden, um dies zu erreichen.
        // Der folgende Code ist ein Beispiel, wie du die AccountManager-API verwenden könntest (muss angepasst werden):
        return "UserName"
        // Überprüfen, ob die GET_ACCOUNTS-Berechtigung gewährt ist (nur für Android 6.0 und höher erforderlich)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    theContext,
                    "android.permission.GET_ACCOUNTS"
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val accountManager = AccountManager.get(theContext)
                val accountArray: Array<Account> = accountManager.accounts

                if (accountArray.isNotEmpty()) {
                    return accountArray[0].name
                }
            } else {
                // GET_ACCOUNTS-Berechtigung wurde nicht gewährt
                // Hier kannst du Logik implementieren, um den Benutzer um Erlaubnis zu bitten.
            }
        } else {
            // Für Versionen unter Android 6.0 benötigst du keine Berechtigung
            val accountManager = AccountManager.get(theContext)
            val accountArray: Array<Account> = accountManager.accounts

            if (accountArray.isNotEmpty()) {
                return accountArray[0].name
            }
        }

        return null
    }
}