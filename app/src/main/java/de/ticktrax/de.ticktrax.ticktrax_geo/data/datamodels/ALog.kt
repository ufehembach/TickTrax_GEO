package de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ALog_TBL_NAME = "tblAlog"

@Entity(tableName = ALog_TBL_NAME)
data class ALog(
    @PrimaryKey(autoGenerate = true)
    var aLogId: Int = 0,
    var dateTime: String? = null,
    var type: String? = null,
    var logText: String? = null,
    var logDetail: String? = null,
)