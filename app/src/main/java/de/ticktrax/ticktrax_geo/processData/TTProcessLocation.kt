package de.ticktrax.ticktrax_geo.processData

import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace_Distance_Max
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
            ttApRep.addLogEntry(
                ALogType.GEO,
                "ProcessLocation:" + newLocation.LocationId
            )
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
                    "ProcessLocation: Di " + distance
                )
                return oldLocation
            }
        }

        fun ProcessLocationDetail(
            oldLocation: TTLocation, newLocation: TTLocation,
            oldLocationDetail: TTLocationDetail, newLocationDetail: TTLocationDetail
        ): TTLocationDetail {
            logDebug("ufe-calc", "check for changes on details")
            ttApRep.addLogEntry(
                ALogType.GEO,
                "ProcessLocationDetail:" + newLocationDetail.LocationDetailId
            )
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
                return oldLocationDetail
            }
            return newLocationDetail
        }

        fun getDifferenceInMinutes(date1: Date, date2: Date): Long {
            val diffInMilliseconds = date2.time - date1.time
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)
        }

        fun ProcessOSMPlace(
            oldOSMPlace: OSMPlace, newOSMPlace: OSMPlace,
        ): OSMPlace {
            ttApRep.addLogEntry(
                ALogType.GEO,
                "ProcessOSMPlace:" + newOSMPlace.osmPlaceId
            )
            // hat sich die geändert?
            if (oldOSMPlace?.osmPlaceId != newOSMPlace.osmPlaceId) {
                logDebug("ufe-calc", "OSMPlaceID Changed")
                return newOSMPlace
            } else {
                // hat sich die OSMPlace nur leicht geändert ?
                val distance = GeoUtils.calculateDistance(
                    oldOSMPlace.lat!!.toDouble(),
                    oldOSMPlace.lon!!.toDouble(),
                    newOSMPlace.lat!!.toDouble(),
                    newOSMPlace.lon!!.toDouble()
                )
                logDebug("ufe-calc", "OSMPlace is ${distance} appart")
                if (distance > OSMPlace_Distance_Max) {
                    logDebug("ufe-calc", "this is a new OSMPlace")
                    return newOSMPlace
                }
                oldOSMPlace.lastDistance = distance
                logDebug("ufe-calc", "distance " + distance.toString())
                ttApRep.addLogEntry(
                    ALogType.GEO,
                    "ProcessOSMPlace: Di " + distance
                )
                return oldOSMPlace
            }
        }

            fun ProcessOSMPlaceDetail(
            oldOSMPlace: OSMPlace, newOSMPlace: OSMPlace,
            oldOSMPlaceDetail: OSMPlaceDetail, newOSMPlaceDetail: OSMPlaceDetail
        ): OSMPlaceDetail {
            ttApRep.addLogEntry(
                ALogType.GEO,
                "ProcessOSMPlaceDetails: " + newOSMPlaceDetail.osmPlaceDetailId
            )
            // hat sich der hash geändert?
            if (oldOSMPlaceDetail?.osmPlaceDetailId != newOSMPlaceDetail.osmPlaceDetailId) {
                logDebug("ufe-calc", "OSMPlaceID Changed")
            } else {
                oldOSMPlaceDetail.lastSeen = Date()
                val duration =
                    getDifferenceInMinutes(oldOSMPlaceDetail.firstSeen, newOSMPlaceDetail.lastSeen)
                oldOSMPlaceDetail.durationMinutes = duration
                logDebug("ufe-calc", "duration " + duration.toString())
                ttApRep.addLogEntry(
                    ALogType.GEO,
                    "ProcessOSMPlace: Du " + duration
                )
                return oldOSMPlaceDetail
            }
            return newOSMPlaceDetail
        }
    }
}

