package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.ticktrax_geo.data.remote.Flatten
import androidx.annotation.Nullable; // Import this for @Nullable annotation

const val TTAggregation_TBL_NAME = "tblTTAggregation"

@Entity(tableName = TTAggregation_TBL_NAME)
data class TTAggregation(
    @PrimaryKey(autoGenerate = true)
    val TTAggregId: Long,

    var lastSeen: String? = null,
    var noOfSights: Long? = null,
    var firstSeen: String? = null,

    var placeID: Long? = null,
    var totalTime: Long? = null
)
