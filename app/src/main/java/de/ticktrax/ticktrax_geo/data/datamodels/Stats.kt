package de.ticktrax.ticktrax_geo.data.datamodels

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

data class Stats(
    var minFirstSeen: Date,
    var maxLastSeen: Date,
    var noPlaces: Long,
    var noLocation: Long,
    var noPlacesNot0: Long,
    var noLocationNot0: Long,
    var durationPlaces:Long,
    var durationLocation: Long
)