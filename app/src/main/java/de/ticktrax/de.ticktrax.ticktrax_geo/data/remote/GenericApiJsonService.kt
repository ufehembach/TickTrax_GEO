package de.ticktrax.ticktrax_geo.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.ticktrax.ticktrax_geo.data.datamodels.GenericEnvelope
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Die Konstante enthält die URL der API



const val BASE_URL = "https://ticktrax.de/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TemplateJsonApiService {
    @GET("h1Motion/cam.json")
    suspend fun getGenericEnvelope() : GenericEnvelope
}

object TemplateJsonApi {
    val apiJsonService: TemplateJsonApiService by lazy { retrofitJson.create(TemplateJsonApiService::class.java) }
}

