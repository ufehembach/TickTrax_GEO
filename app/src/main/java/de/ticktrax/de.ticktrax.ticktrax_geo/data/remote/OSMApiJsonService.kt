package de.ticktrax.ticktrax_geo.data.remote

import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import de.ticktrax.de.ticktrax.ticktrax_geo.data.remote.FlattenTypeAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import retrofit2.converter.gson.GsonConverterFactory

// Die Konstante enthält die URL der API
const val BASE_URL = "https://nominatim.openstreetmap.org/"

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()

object YourCustomGson {
    fun create(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
            .create()
    }
}

val gson = GsonBuilder()
    .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
    .create()

    //.addConverterFactory(MoshiConverterFactory.create(moshi))
private val retrofitGson = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface OSMGsonApiService {
    //@GET("users/{id}")
    //suspend fun getUserById(@Path("id") userID: Int) : User
    @GET("reverse?format=json&lat={GPSLat}&lon={GPSLon}")
    suspend fun getOSMPlace(@Path("GPSLat") lat: Double, @Path("GPSLon") lon: Double): OSMPlace

}

object OSMGsonApi {
    val apiGsonService: OSMGsonApiService by lazy { retrofitGson.create(OSMGsonApiService::class.java) }
}

