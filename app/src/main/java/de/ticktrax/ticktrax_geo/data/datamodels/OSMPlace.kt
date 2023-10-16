package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.ticktrax_geo.data.remote.Flatten
import androidx.annotation.Nullable; // Import this for @Nullable annotation

const val OSMPLACES_TBL_NAME = "tblOSMPlaces"

@Entity(tableName = OSMPLACES_TBL_NAME)
data class OSMPlace(
    @PrimaryKey
    @SerializedName("place_id") val placeId: Long,
    //license

    var lastSeen: String? = null,
    var noOfSights: Long? = null,
    var firstSeen: String? = null,

    var licence: String? = null,
//    var osm_type: String,
    @SerializedName("osm_type") val osmType: String? = null,
//    var osm_id: String,
    @SerializedName("osm_id") val osmId: Long,
    var lat: String,
    var lon: String,
//    var class : String,
    @SerializedName("class") val osmClass: String? = null,
    var type: String,
//    var place_rank: String,
    @SerializedName("place_rank") val placeRank: Long,
    var importance: Double,
    var addresstype: String?=null,
    var name: String,
//    var display_name: String,
    @SerializedName("display_name") val displayName: String? = null,
// flatten address
//    var house_number: String,
    @Flatten("address::house_number") val houseNumber: String? = null,
//    var road: String,
    @Flatten("address::road") val road: String? = null,
//    var hamlet: String,
    @Flatten("address::hamlet") val hamlet: String? = null,
//    var town: String,
    @Flatten("address::town") val town: String? = null,
//    var county: String? = null,
    @Flatten("address::county") val county: String? = null,
//    var state: String? = null,
    @Flatten("address::state") val state: String? = null,
//    var ISO3166-2-lvl4 :String? = null,
    @Flatten("address::ISO3166-2-lvl4") val ISO3166: String? = null,
//    var postcode: String? = null,
    @Flatten("address::postcode") val postcode: String? = null,
//    var country: String? = null,
    @Flatten("address::country") val country: String? = null,
//    var country_code: String? = null,
    @Flatten("address::country_code") val countryCode: String? = null,
// flatten boundingbox
    @Flatten("boundingbox::0") val bb0: String? = null,
    @Flatten("boundingbox::1") val bb1: String? = null,
    @Flatten("boundingbox::2") val bb2: String? = null,
    @Flatten("boundingbox::3") val bb3: String? = null
)
