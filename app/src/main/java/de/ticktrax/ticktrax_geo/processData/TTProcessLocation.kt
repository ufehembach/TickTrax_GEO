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
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel


class TTProcess {
    companion object {
        private val ttApRep = ttRepositoryProvider.TickTraxRepository
        fun ProcessLocation(
            ttOldTTLocation: TTLocation, ttNewOrUpdatedLocation: TTLocation,
            ttOldTTLocationDetail: TTLocationDetail, ttNewOrUpdatedLocationDetail: TTLocationDetail
        ): TTLocationDetail {
            // hat sich der hash geändert?
            if (ttOldTTLocationDetail?.LocationDetailId != ttNewOrUpdatedLocationDetail.LocationDetailId) {
                logDebug("ufe-calc", "LocationID Changed")
            } else {
                // hat sich die location nur leicht geändert ?
                val distance = GeoUtils.calculateDistance(
                    ttOldTTLocation.lat,
                    ttOldTTLocation.lon,
                    ttNewOrUpdatedLocation.lat,
                    ttNewOrUpdatedLocation.lon
                )
                logDebug("ufe-calc", "Location is ${distance} appart")
                if (distance > TTLocation_Distance_Max) {
                    logDebug("ufe-calc", "this is a new location")
                    return ttNewOrUpdatedLocationDetail
                }
                ttOldTTLocationDetail.lastSeen = Date()
                val duration =
                    getDifferenceInMinutes(ttOldTTLocationDetail.firstSeen, ttNewOrUpdatedLocationDetail.lastSeen)
                ttOldTTLocationDetail.durationMinutes = duration
                logDebug("ufe-calc", "duration " + duration.toString())
                ttOldTTLocationDetail.lastDistance = distance
                logDebug("ufe-calc", "distance " + distance.toString())
                ttApRep.addLogEntry(
                    ALogType.GEO,
                    "ProcessLocation: Du " + duration + " Di " + distance
                )
                return  ttOldTTLocationDetail
            }
            return ttNewOrUpdatedLocationDetail
        }

        fun getDifferenceInMinutes(date1: Date, date2: Date): Long {
            val diffInMilliseconds = date2.time - date1.time
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)
        }
    }
}

