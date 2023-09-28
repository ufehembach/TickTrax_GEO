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
import retrofit2.http.Query

// Die Konstante enthält die URL der API
const val BASE_URL = "https://nominatim.openstreetmap.org/"

object YourCustomGson {
    fun create(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
            .create()
    }
}

val gson = GsonBuilder()
    .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
    //.serializeNulls()
    .create()

private val retrofitGson = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface OSMGsonApiService {
    @GET("reverse?format=json")
    suspend fun getOSMPlace(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): OSMPlace

}

object OSMGsonApi {
    val apiGsonService: OSMGsonApiService by lazy { retrofitGson.create(OSMGsonApiService::class.java) }
}

