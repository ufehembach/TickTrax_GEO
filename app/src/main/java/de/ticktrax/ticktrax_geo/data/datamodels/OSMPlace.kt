package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.ticktrax_geo.data.remote.Flatten
import androidx.annotation.Nullable; // Import this for @Nullable annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ExportOrder(val order: Int)
const val OSMPLACES_TBL_NAME = "tblOSMPlaces"

@Entity(tableName = OSMPLACES_TBL_NAME)
data class OSMPlace(
    @PrimaryKey
    @ExportOrder(1)
    @SerializedName("place_id") val placeId: Long,
    //license

    @ExportOrder(2)
    var lastSeen: String? = null,

    @ExportOrder(3)
    var noOfSights: Long? = null,

    @ExportOrder(4)
    var firstSeen: String? = null,

    @ExportOrder(5)
    var myName: String? = null,

    @ExportOrder(6)
    var licence: String? = null,
//    var osm_type: String,

    @ExportOrder(7)
    @SerializedName("osm_type") val osmType: String? = null,

//    var osm_id: String,
    @ExportOrder(8)
    @SerializedName("osm_id") val osmId: Long,

    @ExportOrder(9)
    var lat: String,

    @ExportOrder(10)
    var lon: String,
//    var class : String,

    @ExportOrder(11)
    @SerializedName("class") val osmClass: String? = null,

    @ExportOrder(12)
    var type: String,
//    var place_rank: String,

    @ExportOrder(13)
    @SerializedName("place_rank") val placeRank: Long,

    @ExportOrder(14)
    var importance: Double,

    @ExportOrder(15)
    var addresstype: String?=null,

    @ExportOrder(16)
    var name: String,

    @ExportOrder(17)
//    var display_name: String,
    @SerializedName("display_name") val displayName: String? = null,
// flatten address
//    var house_number: String,

    @ExportOrder(18)
    @Flatten("address::house_number") val houseNumber: String? = null,
//    var road: String,

    @ExportOrder(19)
    @Flatten("address::road") val road: String? = null,
//    var hamlet: String,

    @ExportOrder(20)
    @Flatten("address::hamlet") val hamlet: String? = null,
//    var town: String,

    @ExportOrder(21)
    @Flatten("address::town") val town: String? = null,
//    var county: String? = null,

    @ExportOrder(22)
    @Flatten("address::county") val county: String? = null,
//    var state: String? = null,

    @ExportOrder(23)
    @Flatten("address::state") val state: String? = null,
//    var ISO3166-2-lvl4 :String? = null,

    @ExportOrder(24)
    @Flatten("address::ISO3166-2-lvl4") val ISO3166: String? = null,
//    var postcode: String? = null,

    @ExportOrder(25)
    @Flatten("address::postcode") val postcode: String? = null,
//    var country: String? = null,

    @ExportOrder(26)
    @Flatten("address::country") val country: String? = null,
//    var country_code: String? = null,

    @ExportOrder(27)
    @Flatten("address::country_code") val countryCode: String? = null,
// flatten boundingbox

    @ExportOrder(28)
    @Flatten("boundingbox::0") val bb0: String? = null,

    @ExportOrder(29)
    @Flatten("boundingbox::1") val bb1: String? = null,

    @ExportOrder(30)
    @Flatten("boundingbox::2") val bb2: String? = null,

    @ExportOrder(31)
    @Flatten("boundingbox::3") val bb3: String? = null
)
