package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "genericdata_table")
 data class GenericData(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
//    var dateAdded: OffsetDateTime? = now(),
    var name: String,
    var imagetext: String,
    var image: String,
    var movietext: String,
    var movie: String
)