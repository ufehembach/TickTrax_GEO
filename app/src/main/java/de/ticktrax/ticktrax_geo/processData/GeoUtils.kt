package de.ticktrax.ticktrax_geo.processData

import android.util.Log
import de.ticktrax.ticktrax_geo.myTools.logDebug
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.*

class GeoUtils {
    companion object {
        private const val EARTH_RADIUS = 6371000.0 // Radius der Erde in Metern

        fun calculateDistance(
            lat1: Double, lon1: Double,
            lat2: Double, lon2: Double
        ): Double {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)

            if (1 == 0) {
                logDebug("ufe-calc", ": 1 " + lat1 + " " + lon1)
                logDebug("ufe-calc", ": 2 " + lat2 + " " + lon2)
                logDebug("ufe-calc", ": d " + dLat + " " + dLon)
            }
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            if (1 == 0) {
                logDebug("ufe-calc", ": a" + a + " c " + c)
                logDebug("ufe-calc", ": " + EARTH_RADIUS * c + "m")
            }
            return EARTH_RADIUS * c
        }
    }
}
