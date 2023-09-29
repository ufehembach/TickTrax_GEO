package de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.de.ticktrax.ticktrax_geo.data.remote.Flatten

const val LonLatAlt_TBL_NAME = "tblLonLatAlt"

@Entity(tableName = LonLatAlt_TBL_NAME)
data class LonLatAltRoom(
    @PrimaryKey(autoGenerate = true)
    var lonLatAltId: Int = 0,

    var lastSeen: String? = null,
    var noOfSights: Long? = null,
    var firstSeen: String? = null,

    var lon: Double,
    var lat: Double,
    var alt: Double,



    )
