package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ticktrax.ticktrax_geo.data.Converters
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import java.util.Date

//import com.squareup.moshi.Json

const val TTLocation_TBL_NAME = "tblLocation"

@Entity(tableName = TTLocation_TBL_NAME)
@TypeConverters(Converters::class)
data class TTLocation(
    @PrimaryKey(autoGenerate = false)
    var LocationId: Long,

    var lastSeenS: String? = null,
    var lastSeen: Date,
    var noOfSights: Long? = null,
    var firstSeenS: String? = null,
    var firstSeen: Date,

    var lon: Double,
    var lat: Double,
    var alt: Double,
    )
