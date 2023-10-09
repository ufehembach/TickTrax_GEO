package de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ALog_TBL_NAME = "tblAlog"
const val ALog_ROOM_Max = 1000
@Entity(tableName = ALog_TBL_NAME)
data class ALog(
    @PrimaryKey(autoGenerate = true)
    var aLogId: Int = 0,
    var dateTime: String? = null,
    var logType: String? = null,
    var logText: String? = null,
    var logDetail: String? = null,

)