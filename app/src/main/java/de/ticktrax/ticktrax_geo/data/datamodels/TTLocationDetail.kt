package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ticktrax.ticktrax_geo.data.Converters
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import org.osmdroid.util.Distance
import java.util.Date

//import com.squareup.moshi.Json

const val TTLocationDetail_TBL_NAME = "tblLocationDetail"

@Entity(tableName = TTLocationDetail_TBL_NAME)
@TypeConverters(Converters::class)
data class TTLocationDetail(
    @PrimaryKey(autoGenerate = true)
    @ExportOrder(1)
    var LocationDetailId: Long = 0,

    @ExportOrder(2)
    var LocationId: String = "",

    @ExportOrder(3)
    var lastSeen: Date = Date(0, 0, 0),

    @ExportOrder(4)
    var firstSeen: Date = Date(0, 0, 0),

    @ExportOrder(5)
    var durationMinutes: Long? = 0,

    @ExportOrder(6)
    var lastDistance: Double = 0.0
)
