package de.ticktrax.ticktrax_geo.processData

import android.util.Log
import androidx.activity.viewModels
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation_Distance_Max
import java.util.Date
import java.util.*
import java.util.concurrent.TimeUnit
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel


class TTProcess {
    companion object {
        fun ProcessLocation(
            ttOldLocation: TTLocation, ttNewOrUpdatedLocation: TTLocation
        ): TTLocation {
            // hat sich der hash geändert?
            if (ttOldLocation.LocationId != ttNewOrUpdatedLocation.LocationId) {
                Log.d("ufe-calc", "LocationID Changed")
            }
            // hat sich die location nur leicht geändert ?
            val distance = GeoUtils.calculateDistance(
                ttOldLocation.lat,
                ttOldLocation.lon,
                ttNewOrUpdatedLocation.lat,
                ttNewOrUpdatedLocation.lon
            )
            Log.d("ufe-calc", "Location is ${distance} appart")
            if (distance > TTLocation_Distance_Max) {
                Log.d("ufe-calc", "this is a new location")
                return ttNewOrUpdatedLocation
            }
            ttOldLocation.lastSeen = Date()
            ttOldLocation.durationMinutes =
                getDifferenceInMinutes(ttOldLocation.firstSeen, ttNewOrUpdatedLocation.lastSeen)
            ttOldLocation.lastDistance = distance
            return ttOldLocation

//            val oldLat = 37.7749
//            val oldLon = -122.4194
//            val newLat = 34.0522
//            val newLon = -118.2437
//            val distance = GeoUtils.calculateDistance(oldLat, oldLon, newLat, newLon)
//            println("Die Distanz beträgt $distance Meter.")
        }

        fun getDifferenceInMinutes(date1: Date, date2: Date): Long {
            val diffInMilliseconds = date2.time - date1.time
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)
        }
    }
}

