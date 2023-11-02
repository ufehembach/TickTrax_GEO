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
    var LocationDetailId: Long = 0,
    var LocationId: Long = 0,
    var lastSeen: Date = Date(0, 0, 0),
    var firstSeen: Date = Date(0, 0, 0),
    var durationMinutes: Long? = 0,
    var lastDistance: Double = 0.0
)
