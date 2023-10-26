package de.ticktrax.ticktrax_geo.myTools


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import jxl.Workbook
import jxl.write.Label
import java.io.*

data class YourEntity(val id: Int, val name: String, val age: Int)

class Export : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: List<YourEntity> = getDataFromRoom() // Replace with your Room query

        // Export as JSON
        exportAsJson(data, "exported_data.json")

        // Export as XLS
        exportAsXls(data, "exported_data.xls")

        // Export as CSV
        exportAsCsv(data, "exported_data.csv")
    }

    private fun getDataFromRoom(): List<YourEntity> {
        // Replace this with your Room database query to fetch data
        return emptyList()
    }

    private fun exportAsJson(data: List<YourEntity>, fileName: String) {
        val jsonContent = Gson().toJson(data)
        saveToFile(jsonContent, fileName, "json")
        sendEmailWithAttachment(fileName, "application/json")
    }

    private fun exportAsXls(data: List<YourEntity>, fileName: String) {
        val xlsFilePath = "${getExternalFilesDir(null)}/$fileName"
        createXlsFileFromData(data, xlsFilePath)
        sendEmailWithAttachment(xlsFilePath, "application/vnd.ms-excel")
    }

    private fun exportAsCsv(data: List<YourEntity>, fileName: String) {
        val csvFilePath = "${getExternalFilesDir(null)}/$fileName"
        createCsvFileFromData(data, csvFilePath)
        sendEmailWithAttachment(csvFilePath, "text/csv")
    }

    private fun saveToFile(content: String, fileName: String, extension: String) {
        val filePath = "${getExternalFilesDir(null)}/$fileName"
        val file = File(filePath)
        BufferedWriter(FileWriter(file)).use { writer ->
            writer.write(content)
        }
        sendEmailWithAttachment(filePath, "application/$extension")
    }

    private fun createXlsFileFromData(data: List<YourEntity>, filePath: String) {
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

    private fun createCsvFileFromData(data: List<YourEntity>, filePath: String) {
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

        val uri = FileProvider.getUriForFile(this, "your.package.name.fileprovider", File(filePath))
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
}
