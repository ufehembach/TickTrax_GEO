package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.ticktrax_geo.data.remote.Flatten
import androidx.annotation.Nullable; // Import this for @Nullable annotation
import androidx.room.TypeConverters
import de.ticktrax.ticktrax_geo.data.Converters
import java.util.Date

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ExportOrder(val order: Int)

const val OSMPlace_TBL_NAME = "tblOSMPlace"
const val OSMPlace_Distance_Max = 25 // in Meter gives what we see as the same location

@Entity(tableName = OSMPlace_TBL_NAME)
@TypeConverters(Converters::class)
data class OSMPlace(
    @PrimaryKey
    @ExportOrder(1)
    @SerializedName("place_id") val OSMPlaceId: Long,

//    var osm_id: String,
    @ExportOrder(2)
    @SerializedName("osm_id") val osmId: Long,

    @ExportOrder(3)
    var lat: Double,
//var lat: String,

    @ExportOrder(4)
    var lon: Double,
//    var lon: String,
//    var class : String,

    @ExportOrder(5)
    var myName: String? = null,

//license
    @ExportOrder(6)
    var licence: String? = null,
//    var osm_type: String,

    @ExportOrder(7)
    @SerializedName("osm_type")
    val osmType: String? = null,

    @ExportOrder(8)
    @SerializedName("class")
    val osmClass: String? = null,

    @ExportOrder(9)
    var type: String? = null,
//    var place_rank: String,

    @ExportOrder(10)
    @SerializedName("place_rank")
    val placeRank: Long? = 0L,

    @ExportOrder(11)
    var importance: Double? = 0.0,

    @ExportOrder(12)
    var addresstype: String? = null,

    @ExportOrder(13)
    var name: String? = null,

    @ExportOrder(14)
//    var display_name: String,
    @SerializedName("display_name")
    val displayName: String? = null,
// flatten address
//    var house_number: String,

    @ExportOrder(15)
    @Flatten("address::house_number")
    val houseNumber: String? = null,
//    var road: String,

    @ExportOrder(16)
    @Flatten("address::road")
    val road: String? = null,
//    var hamlet: String,

    @ExportOrder(17)
    @Flatten("address::hamlet")
    val hamlet: String? = null,
//    var town: String,

    @ExportOrder(18)
    @Flatten("address::town")
    val town: String? = null,
//    var county: String? = null,

    @ExportOrder(19)
    @Flatten("address::county")
    val county: String? = null,
//    var state: String? = null,

    @ExportOrder(20)
    @Flatten("address::state")
    val state: String? = null,
//    var ISO3166-2-lvl4 :String? = null,

    @ExportOrder(21)
    @Flatten("address::ISO3166-2-lvl4")
    val ISO3166: String? = null,
//    var postcode: String? = null,

    @ExportOrder(22)
    @Flatten("address::postcode")
    val postcode: String? = null,
//    var country: String? = null,

    @ExportOrder(23)
    @Flatten("address::country")
    val country: String? = null,
//    var country_code: String? = null,

    @ExportOrder(24)
    @Flatten("address::country_code")
    val countryCode: String? = null,
// flatten boundingbox

    @ExportOrder(25)
    @Flatten("boundingbox::0")
    val bb0: String? = null,

    @ExportOrder(26)
    @Flatten("boundingbox::1")
    val bb1: String? = null,

    @ExportOrder(27)
    @Flatten("boundingbox::2")
    val bb2: String? = null,

    @ExportOrder(28)
    @Flatten("boundingbox::3")
    val bb3: String? = null,

    @ExportOrder(29)
    var firstSeen: Date = Date(0, 0, 0),

    @ExportOrder(30)
    var lastSeen: Date = Date(0, 0, 0),

    @ExportOrder(31)
    var lastDistance: Double = 0.0
)
