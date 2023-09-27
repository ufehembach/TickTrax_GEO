package de.ticktrax.ticktrax_geo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.LonLatAltRoom
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi

// todo
/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von Live Data dem Rest
 * der App zur Verfügung
 */
class TickTraxAppRepository(
    private val OSMGsonApi: OSMGsonApi,
    private val database: TickTraxDB
    // private val TemplateTxtApi: TemplateTxtApi
) {
//    private val locationRepository = RepositoryProvider.locationRepository

    init{
        Log.d("ufe ", "TickTraxAppRepository -> INIT")
        getAllLonLatAlt()
    }
    private var _OSMPlace = MutableLiveData<OSMPlace>()
    val OSMPlace: LiveData<OSMPlace>
        get() = _OSMPlace

    suspend fun getPlaceFromOSM(lat: Double, lon: Double) {
        try {
            Log.d("ufe", "load Data from API")
            val gsonOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
            //          locationRepository.locationData.value!!.latitude,
            //          locationRepository.locationData.value!!.longitude
            //     )
            Log.d("ufe", "Data from API ")
            //      Log.d("ufe", "Data from API " + _OSMPlace.value.media.size)
            _OSMPlace.postValue(gsonOSMPlace)
            database.TickTraxDao.insertOSMPlace(gsonOSMPlace)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }

    private var _OSMPlaces = MutableLiveData<List<OSMPlace>>()
    val OSMPlaces: LiveData<List<OSMPlace>>
        get() = _OSMPlaces

    suspend fun getAllOSMPlacesFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllOSMPlaces()
            Log.d("ufe", "I read " + allPlaces.size + " Records from ROOM")
            _OSMPlaces.postValue(allPlaces)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }

    // -------------------------------------------
    private var _lonLatAltRoom = MutableLiveData<LonLatAltRoom>()
    val lonLatAltRoom: LiveData<LonLatAltRoom>
        get() = _lonLatAltRoom

    suspend fun saveOneLonLatAlt(lon: Double, lat: Double, alt: Double) {
        try {
            Log.d("ufe", "save lonlatalt Data from room")
            var lonLatAlt = LonLatAltRoom(0, lon, lat, alt)
            database.TickTraxDao.insertLonLatAlt((lonLatAlt))
        } catch (e: Exception) {
            Log.e("ufe", "error saving date from API: $e")
        }
    }

    private var _lonLatAltRoomS = MutableLiveData<List<LonLatAltRoom>>()
    val lonLatAltRoomS: LiveData<List<LonLatAltRoom>>
        get() = _lonLatAltRoomS

    suspend fun getAllLonLatAlt() {
        try {
            Log.d("ufe", "load lonlatalt Data from room")
            _lonLatAltRoomS.postValue(database.TickTraxDao.getAllLonLatAlt())
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }
}


