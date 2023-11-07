package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ticktrax.ticktrax_geo.data.Converters
//import com.squareup.moshi.Json
import java.util.Date

const val OSMPlaceDetail_TBL_NAME = "tblOSMPlaceDetail"

@Entity(tableName = OSMPlaceDetail_TBL_NAME)
@TypeConverters(Converters::class)
data class OSMPlaceDetail(
    @PrimaryKey(autoGenerate = true)
    @ExportOrder(1)
    var OSMPlaceDetailId: Long? = 0L,

    @ExportOrder(2)
    var OSMPlaceId: Long? = 0L,

    @ExportOrder(3)
    var lastSeen: Date = Date(0, 0, 0),

    @ExportOrder(4)
    var firstSeen: Date = Date(0, 0, 0),

    @ExportOrder(5)
    var durationMinutes: Long? = 0,

    @ExportOrder(6)
    var lastDistance: Double? = 0.0
)
