package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json

const val TTLocation_TBL_NAME = "tblLocation"

@Entity(tableName = TTLocation_TBL_NAME)
data class TTLocation(
    @PrimaryKey(autoGenerate = true)
    var LocationId: Int = 0,

    var lastSeen: String? = null,
    var noOfSights: Long? = null,
    var firstSeen: String? = null,

    var lon: Double,
    var lat: Double,
    var alt: Double,
    )
