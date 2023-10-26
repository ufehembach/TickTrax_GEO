package de.ticktrax.ticktrax_geo.myTools

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context, private val activity: Activity) {

    companion object {
        const val GET_ACCOUNTS_PERMISSION_REQUEST_CODE = 1001
    }

    fun requestGetAccountsPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.GET_ACCOUNTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Wenn die Berechtigung nicht vorhanden ist, zeige einen Erklärungstext an
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.GET_ACCOUNTS
                )
            ) {
                // Hier kannst du eine Erklärung anzeigen, warum du die Berechtigung benötigst

            } else {
                // Wenn die Berechtigung noch nicht gewährt wurde, fordere sie an
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.GET_ACCOUNTS),
                    GET_ACCOUNTS_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // Die Berechtigung ist bereits erteilt
            // Hier kannst du deinen Code ausführen, der die Berechtigung benötigt
        }
    }

    // Diese Methode wird aufgerufen, wenn der Benutzer auf die Berechtigungsanfrage reagiert
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            GET_ACCOUNTS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Berechtigung wurde erteilt
                    // Führe hier deinen Code aus, der die Berechtigung benötigt
                } else {
                    // Berechtigung wurde verweigert
                    // Hier kannst du entsprechend reagieren
                }
            }
            // Füge hier weitere Berechtigungsanfragen hinzu, wenn nötig
        }
    }
}
