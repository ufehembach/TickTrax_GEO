package de.ticktrax.ticktrax_geo.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.LonLatAltRoom
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// todo
/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von Live Data dem Rest
 * der App zur Verfügung
 */

object ttRepositoryProvider {
    val TickTraxRepository by lazy { TickTraxAppRepository() }
}

class TickTraxAppRepository {

    private lateinit var oSMGsonApi: OSMGsonApi
    private lateinit var database: TickTraxDB

    init {
        Log.d("ufe ", "TickTraxAppRepository -> INIT")
        // getAllLonLatAlt()
    }

    public fun setParas(pOSMGsonApi: OSMGsonApi, pDatabase: TickTraxDB) {
        oSMGsonApi = pOSMGsonApi
        database = pDatabase
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

    // -------------------------
    // from sharedrepository
    private val _locationData = MutableLiveData<Location>()

    val locationData: LiveData<Location>
        get() = _locationData

    //fun setLocation(latitude: Double, longitude: Double) {
    fun setLocation(myLocation: Location) {
        _locationData.postValue(myLocation)
        var lonLatAlt: LonLatAltRoom =
            LonLatAltRoom(0, myLocation.longitude, myLocation.latitude, myLocation.altitude)
        Log.d("ufe", "LonLatAltRoom:" + myLocation.toString())
        database.TickTraxDao.insertLonLatAlt((lonLatAlt))
        Log.d("ufe", "after db insert:" + myLocation.toString())
        // Starten Sie eine Coroutine auf dem Dispatchers.IO-Thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Führen Sie hier asynchrone Aufgaben aus, z.B. eine Netzwerkanfrage
                getPlaceFromOSM(myLocation.latitude,myLocation.longitude)
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
            getAllOSMPlacesFromRoom()
        }
    }

}


