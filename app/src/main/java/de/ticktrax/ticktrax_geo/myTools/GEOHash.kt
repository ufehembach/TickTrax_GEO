package de.ticktrax.ticktrax_geo.myTools

import android.location.Location

//fun GEOHash(float1: Float, float2: Float):Long  {
fun GEOHash(myLocation:Location):Long  {
    val prime = 31
    var result = 1L
    result = prime * result + myLocation.latitude.hashCode()
    result = prime * result + myLocation.longitude.hashCode()
    return result
}
