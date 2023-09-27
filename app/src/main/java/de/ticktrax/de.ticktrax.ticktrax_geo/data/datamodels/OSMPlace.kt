package de.ticktrax.ticktrax_geo.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName
import de.ticktrax.de.ticktrax.ticktrax_geo.data.remote.Flatten

const val OSMPLACES_TBL_NAME="tblOSMPlaces"

@Entity(tableName = OSMPLACES_TBL_NAME)
data class OSMPlace(
    @PrimaryKey
    @SerializedName("place_id") val placeId: Long,
    //license
    var licence: String,
//    var osm_type: String,
    @SerializedName("osm_type") val osmType: Long,
//    var osm_id: String,
    @SerializedName("osm_id") val osmId: Long,
   var lat: String,
   var lon: String,
//    var class : String,
    @SerializedName("class") val osmClass: Long,
   var    type :String,
//    var place_rank: String,
    @SerializedName("place_rank") val placeRank: Long,
    var importance: String,
    var addresstype: String,
    var name: String,
//    var display_name: String,
    @SerializedName("display_name") val displayName: String,
// flatten address
//    var house_number: String,
    @Flatten("address::house_number") val houseNumber: String,
//    var road: String,
    @Flatten("address::road") val road: String,
//    var hamlet: String,
    @Flatten("address::hamlet") val hamlet: String,
//    var town: String,
    @Flatten("address::town") val town: String,
//    var county: String,
    @Flatten("address::county") val county: String,
//    var state: String,
    @Flatten("address::state") val state: String,
//    var ISO3166-2-lvl4 :String,
    @Flatten("address::ISO3166-2-lvl4") val ISO3166: String,
//    var postcode: String,
    @Flatten("address::postcode") val postcode: String,
//    var country: String,
    @Flatten("address::country") val country: String,
//    var country_code: String,
    @Flatten("address::country_code") val countryCode: String,
// flatten boundingbox
    @Flatten("boundingbox::0") val bb0: String,
    @Flatten("boundingbox::1") val bb1: String,
    @Flatten("boundingbox::2") val bb2: String,
    @Flatten("boundingbox::3") val bb3: String
)
//internal class sample {
//    @Flatten("temperature::min") var min_temperture = 0
//    @Flatten("temperature::max") var max_temperture = 0
//}
