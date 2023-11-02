package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.ColumnInfo
import androidx.room.Embedded
import de.ticktrax.ticktrax_geo.data.Converters
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import org.osmdroid.util.Distance
import java.util.Date


data class TTLocationExt(
    @Embedded
    val ttLocation: TTLocation,
    @ColumnInfo(name = "durationSum")
    var durationMinutes: Long = 0
)
