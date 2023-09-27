package de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

const val LonLatAlt_TBL_NAME="tblLonLatAlt"

@Entity(tableName = LonLatAlt_TBL_NAME)
data class LonLatAltRoom(
    @PrimaryKey(autoGenerate = true)
    var LonLATALTId: Int = 0,
    var lon : Double,
    var lat : Double,
    var alt: Double

)
