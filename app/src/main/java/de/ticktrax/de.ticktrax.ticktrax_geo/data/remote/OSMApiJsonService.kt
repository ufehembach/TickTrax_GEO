package de.ticktrax.ticktrax_geo.data.remote

import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.ticktrax.de.ticktrax.ticktrax_geo.data.remote.FlattenTypeAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Die Konstante enthält die URL der API


const val BASE_URL = "https://nominatim.openstreetmap.org/"

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()

val gson = GsonBuilder()
    .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
    .create()

    //.addConverterFactory(MoshiConverterFactory.create(moshi))
private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(FlattenTypeAdapterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface OSMJsonApiService {
    //@GET("users/{id}")
    //suspend fun getUserById(@Path("id") userID: Int) : User
    @GET("reverse?format=json&lat={GPSLat}&lon={GPSLon}")
    suspend fun getOSMPlace(@Path("GPSLat") lat: Double, @Path("GPSLon") lon: Double): Double

}

object OSMJsonApi {
    val apiJsonService: OSMJsonApiService by lazy { retrofitJson.create(OSMJsonApiService::class.java) }
}

