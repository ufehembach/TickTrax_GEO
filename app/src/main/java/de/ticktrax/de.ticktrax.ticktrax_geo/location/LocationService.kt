package de.ticktrax.de.ticktrax.ticktrax_geo.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import de.ticktrax.ticktrax_geo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private var _geoLocation = MutableLiveData<Location>()
    val  geoLocation: LiveData<Location>
        get() = _geoLocation

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.opas_ticktrax_app_logo)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()//.takeLast(3)
                val long = location.longitude.toString()//.takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                Log.d("ufe-geo", "Location in LocService: ($lat, $long)")
                _geoLocation.postValue(location)
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
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.location.Location
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.android.gms.location.LocationServices
//import de.ticktrax.ticktrax_geo.R
//import de.ticktrax.ticktrax_geo.data.datamodels.GenericEnvelope
//import de.ticktrax.ticktrax_geo.ui.TemplateViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
//class LocationService : Service() {
//
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private lateinit var locationClient: LocationClient
//
//    private var _geoLocation = MutableLiveData<Location>()
//    val  geoLocation: LiveData<Location>
//        get() = _geoLocation
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        locationClient = DefaultLocationClient(
//            applicationContext,
//            LocationServices.getFusedLocationProviderClient(applicationContext)
//        )
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            ACTION_START -> start()
//            ACTION_STOP -> stop()
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    private fun start() {
//        val notification = NotificationCompat.Builder(this, "location")
//            .setContentTitle("Tracking location...")
//            .setContentText("Location: null")
//            .setSmallIcon(R.drawable.opas_ticktrax_app_logo)
//            .setOngoing(true)
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        var lat: String = ""
//        var long: String = ""
//
//        locationClient
//            .getLocationUpdates(10000L)
//            .catch { e -> e.printStackTrace() }
//            .onEach { location ->
//                lat = location.latitude.toString()//.takeLast(3)
//                long = location.longitude.toString()//.takeLast(3)
//                val updatedNotification = notification.setContentText(
//                    "Location: ($lat, $long)"
//                )
//                Log.d("ufe-geo", "Location: ($lat, $long)")
//                _geoLocation.postValue(location)
//
//                notificationManager.notify(1, updatedNotification.build())
//            }
//            .launchIn(serviceScope)
//
//        startForeground(1, notification.build())
//    }
//
//    private fun stop() {
//        stopForeground(true)
//        stopSelf()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        serviceScope.cancel()
//    }
//
//
//}