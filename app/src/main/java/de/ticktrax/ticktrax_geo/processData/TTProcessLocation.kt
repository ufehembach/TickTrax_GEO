package de.ticktrax.ticktrax_geo.processData

import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation_Distance_Max
import java.util.Date
import java.util.concurrent.TimeUnit
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
import de.ticktrax.ticktrax_geo.myTools.logDebug


class TTProcess {
    companion object {
        private val ttApRep = ttRepositoryProvider.TickTraxRepository
        fun ProcessLocation(
            oldLocation: TTLocation, newLocation: TTLocation,
        ): TTLocation {
            // hat sich der hash geändert?
            if (oldLocation?.LocationId != newLocation.LocationId) {
                logDebug("ufe-calc", "LocationID Changed")
                return newLocation
            } else {
                // hat sich die location nur leicht geändert ?
                val distance = GeoUtils.calculateDistance(
                    oldLocation.lat,
                    oldLocation.lon,
                    newLocation.lat,
                    newLocation.lon
                )
                logDebug("ufe-calc", "Location is ${distance} appart")
                if (distance > TTLocation_Distance_Max) {
                    logDebug("ufe-calc", "this is a new location")
                    return newLocation
                }
                oldLocation.lastDistance = distance
                logDebug("ufe-calc", "distance " + distance.toString())
                ttApRep.addLogEntry(
                    ALogType.GEO,
                    "ProcessLocation:  Di " + distance
                )
                return  oldLocation
            }
        }

        fun ProcessLocationDetail(
            oldLocation: TTLocation, newLocation: TTLocation,
            oldLocationDetail: TTLocationDetail, newLocationDetail: TTLocationDetail
        ): TTLocationDetail {
            logDebug("ufe-calc", "check for changes on details")
            // hat sich der hash geändert?
            if (oldLocationDetail?.LocationDetailId != newLocationDetail.LocationDetailId) {
                logDebug("ufe-calc", "LocationID Changed")
            } else {
                oldLocationDetail.lastSeen = Date()
                val duration =
                    getDifferenceInMinutes(oldLocationDetail.firstSeen, newLocationDetail.lastSeen)
                oldLocationDetail.durationMinutes = duration
                logDebug("ufe-calc", "duration " + duration.toString())
                ttApRep.addLogEntry(
                    ALogType.GEO,
                    "ProcessLocation: Du " + duration
                )
                return  oldLocationDetail
            }
            return newLocationDetail
        }

        fun getDifferenceInMinutes(date1: Date, date2: Date): Long {
            val diffInMilliseconds = date2.time - date1.time
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)
        }
    }
}

