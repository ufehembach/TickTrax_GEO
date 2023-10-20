package de.ticktrax.ticktrax_geo.myTools


import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import jxl.Workbook
import jxl.write.Label
import java.io.*

class ExportViaMail<T : Any>(private val data: List<T>) {
    fun exportAll(filePath: File?) {
        var file = File(filePath, "temp_xls_file.xls")
        // Export as JSON
        exportAsJson(data, file)

        // Export as XLS
        file = File(filePath,"exported_data.xls")
        exportAsXls(data, file)

        // Export as CSV
        file = File(filePath, "exported_data.csv")
        exportAsCsv(data, file)

    }
    fun triggerCallBack(callback:(String,String)->Unit)
    {
        callback("a","b")
    }
    private fun exportAsJson(data: List<T>, fullFile: File) {
        val jsonContent = Gson().toJson(data)
        saveToFile(jsonContent, fullFile.canonicalPath, "json")
        sendEmailWithAttachment(fullFile.canonicalPath, "application/json")
    }

    private fun exportAsXls(data: List<T>, fullFile: File) {
        createXlsFileFromData(data, fullFile.canonicalPath)
        sendEmailWithAttachment(fullFile.canonicalPath, "application/vnd.ms-excel")
    }

    private fun exportAsCsv(data: List<T>, fullFile: File) {
        createCsvFileFromData(data, fullFile.canonicalPath)
        sendEmailWithAttachment(fullFile.canonicalPath, "text/csv")
    }

    private fun saveToFile(content: String, fullFileName: String, extension: String) {
        val file = File(fullFileName)
        BufferedWriter(FileWriter(file)).use { writer ->
            writer.write(content)
        }
        sendEmailWithAttachment(fullFileName, "application/$extension")
    }

    private fun createXlsFileFromData(data: List<T>, filePath: String) {
        val workbook = Workbook.createWorkbook(FileOutputStream(filePath))
        val sheet = workbook.createSheet("Sheet1", 0)

        // Write headers
        data.firstOrNull()?.let { entity ->
            val headers = entity::class.java.declaredFields.map { it.name }
            headers.forEachIndexed { colIndex, header ->
                sheet.addCell(Label(colIndex, 0, header))
            }
        }

        // Write data
        data.forEachIndexed { rowIndex, entity ->
            entity::class.java.declaredFields.forEachIndexed { colIndex, field ->
                field.isAccessible = true
                val cellValue = field.get(entity)?.toString() ?: ""
                sheet.addCell(Label(colIndex, rowIndex + 1, cellValue))
            }
        }

        workbook.write()
        workbook.close()
    }

    private fun createCsvFileFromData(data: List<T>, filePath: String) {
        BufferedWriter(FileWriter(filePath)).use { writer ->
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

    private fun sendEmailWithAttachment(filePath: String, mimeType: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = mimeType

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Exported Data")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attached file.")

//        val uri = FileProvider.getUriForFile( myContext"your.package.name.fileprovider", File(filePath))
  //      emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        //startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
}