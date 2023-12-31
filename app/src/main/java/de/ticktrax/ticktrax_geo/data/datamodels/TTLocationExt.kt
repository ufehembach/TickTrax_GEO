package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.ColumnInfo
import androidx.room.Embedded
import de.ticktrax.ticktrax_geo.data.Converters
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import org.osmdroid.util.Distance
import java.util.Date


data class TTLocationExt(
    @Embedded

    @ExportOrder(1)
    val ttLocation: TTLocation,
    @ColumnInfo(name = "durationSum")
    @ExportOrder(2)
    var durationMinutes: Long = 0
)
