package de.ticktrax.ticktrax_geo.myTools

import android.location.Location

//fun GEOHash(float1: Float, float2: Float):Long  {
fun GEOHash(lat : Double, lon: Double):Long  {
    val prime = 31
    var result = 1L
    result = prime * result + lat.hashCode()
    result = prime * result + lon.hashCode()
    return result
}
