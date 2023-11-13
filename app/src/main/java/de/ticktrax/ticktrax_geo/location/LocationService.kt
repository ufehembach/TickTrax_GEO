package de.ticktrax.ticktrax_geo.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
import de.ticktrax.ticktrax_geo.myTools.logDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    //private val locationRepository = RepositoryProvider.locationRepository
    private val ttApRep = ttRepositoryProvider.TickTraxRepository

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        logDebug("ufe-service", "service on create")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        //ttApRep.addLogEntry(ALogType.FGSERV, "service on startCommand")
        logDebug("ufe-service", "service on onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.opas_ticktrax_app_logo)
            .setOngoing(true)
        //ttApRep.addLogEntry(ALogType.FGSERV, "service on start")
        logDebug("ufe-service","Service Start")

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

                val lat = location.latitude.toString()//.takeLast(3)
                val lon = location.longitude.toString()//.takeLast(3)
                val alt = location.altitude.toString()//.takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $lon, $alt)"
                )
                logDebug("ufe-geo", "Location in LocService: ($lat, $lon)")
                // locationRepository.setLocation(location)
                ttApRep.addLatLonAlt(location.latitude,location.longitude,location.altitude)
                ttApRep.addLogEntry(
                    ALogType.FGSERV,
                    "$lat/$lon/$alt",
                    location.toString()
                )
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
