package de.ticktrax.h1Template.myTools

import android.util.Log
import de.ticktrax.ticktrax_geo.myTools.logDebug
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class ReadTXTfromURL(private var link: String) {
    // val link = "http://.../file.txt"
    private val al = ArrayList<String>()
    fun read(): String {
        logDebug("ufe","in readstring for " +link)
        try {
            val url = URL(link)
            val conn = url.openConnection()
            conn.doOutput = true
            conn.connect()
            val `is` = conn.getInputStream()
            val isr = InputStreamReader(`is`, "UTF-8")
            val br = BufferedReader(isr)
            var line: String
            try {
                while (br.readLine().also { line = it } != null) {
                    al.add(line)
                }
            } finally {
                br.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return al[0]
    }
}