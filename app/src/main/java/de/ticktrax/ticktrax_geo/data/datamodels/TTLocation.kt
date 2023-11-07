package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ticktrax.ticktrax_geo.data.Converters
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import org.osmdroid.util.Distance
import java.util.Date

//import com.squareup.moshi.Json

const val TTLocation_TBL_NAME = "tblLocation"
const val TTLocation_Distance_Max = 10 // in Meter gives what we see as the same location

@Entity(tableName = TTLocation_TBL_NAME)
@TypeConverters(Converters::class)
data class TTLocation(
    @PrimaryKey(autoGenerate = false)
    @ExportOrder(1)
    var LocationId: Long = 0,

    @ExportOrder(2)
    var lon: Double = 0.0,

    @ExportOrder(3)
    var lat: Double = 0.0,

    @ExportOrder(4)
    var alt: Double = 0.0,


    @ExportOrder(5)
    var locAdded: Date = Date(0, 0, 0),

    @ExportOrder(6)
    var lastSeen: Date = Date(0, 0, 0),

    @ExportOrder(7)
    var lastDistance: Double = 0.0
)
