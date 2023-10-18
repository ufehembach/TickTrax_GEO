package de.ticktrax.ticktrax_geo.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.ALog_ROOM_Max
import de.ticktrax.ticktrax_geo.myTools.DateTimeUtils
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.TTAggregation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

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

    }

    public fun setParas(pOSMGsonApi: OSMGsonApi, pDatabase: TickTraxDB) {
        oSMGsonApi = pOSMGsonApi
        database = pDatabase
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Führen Sie hier asynchrone Aufgaben aus, z.B. eine Netzwerkanfrage
                getAllLocationsFromRoom()
                getAllOSMPlacesFromRoom()
                getAllTTAggregationsFromRoom()
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
        }
    }
    // TTAggregation
    private var _TTAggregation = MutableLiveData<TTAggregation>()
    val TTAggregation: LiveData<TTAggregation>
        get() = _TTAggregation

    private var _TTAggregationS = MutableLiveData<List<TTAggregation>>()
    val TTAggregationS: LiveData<List<TTAggregation>>
        get() = _TTAggregationS

    suspend fun getAllTTAggregationsFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllTTAggregations()
            Log.d("ufe", "I read " + allPlaces.size + " TTAggregations from ROOM")
            _TTAggregationS.postValue(allPlaces)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    // OSMPlace
    private var _OSMPlace = MutableLiveData<OSMPlace>()
    val OSMPlace: LiveData<OSMPlace>
        get() = _OSMPlace

    suspend fun getPlaceFromOSM(lat: Double, lon: Double) {
        try {
            Log.d("ufe", "load Data from API")
            val gsonOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
            Log.d("ufe", "OSM Data from API ")
            gsonOSMPlace.firstSeen = DateTimeUtils.formatDateTimeToUTC(Date())
            gsonOSMPlace.lastSeen = DateTimeUtils.formatDateTimeToUTC(Date())
            gsonOSMPlace.noOfSights = 1
            _OSMPlace.postValue(gsonOSMPlace)
            addLogEntry(ALogType.GEO, "OSM Place added", gsonOSMPlace.toString())
            database.TickTraxDao.insertOSMPlace(gsonOSMPlace)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }

    private var _OSMPlaceS = MutableLiveData<List<OSMPlace>>()
    val OSMPlaceS: LiveData<List<OSMPlace>>
        get() = _OSMPlaceS

    suspend fun getAllOSMPlacesFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllOSMPlaces()
            Log.d("ufe", "I read " + allPlaces.size + " OSMPlaces from ROOM")
            _OSMPlaceS.postValue(allPlaces)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    // ---ttLocationData----------------------
    private val _ttLocation = MutableLiveData<TTLocation>()
    val ttLocation: LiveData<TTLocation>
        get() = _ttLocation

    private var _ttLocationS = MutableLiveData<List<TTLocation>>()
    val ttLocationS: LiveData<List<TTLocation>>
        get() = _ttLocationS

    suspend fun getAllLocationsFromRoom() {
        try {
            var allLocations = database.TickTraxDao.getAllLocations()
            Log.d("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
            _ttLocationS.postValue(allLocations)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    fun setLocation(myLocation: Location) {
        val currentDate = Date()
        val formattedDateUTC = DateTimeUtils.formatDateTimeToUTC(Date())
        Log.d("Formatted Date (UTC)", formattedDateUTC)

        var ttLocation: TTLocation =
            TTLocation(
                0,
                formattedDateUTC,
                1,
                formattedDateUTC,
                myLocation.longitude,
                myLocation.latitude,
                myLocation.altitude
            )
        _ttLocation.postValue(ttLocation)
        Log.d("ufe", "Location:" + myLocation.toString())
        database.TickTraxDao.insertLocation((ttLocation))
        addLogEntry(
            ALogType.GEO,
            "Location (${ttLocation.lon},${ttLocation.lat})",
            ttLocation.toString()
        )
        Log.d("ufe", "Location - after db insert:" + ttLocation.toString())
        // Starten Sie eine Coroutine auf dem Dispatchers.IO-Thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Führen Sie hier asynchrone Aufgaben aus, z.B. eine Netzwerkanfrage
                getPlaceFromOSM(ttLocation.lat, ttLocation.lon)
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
            getAllOSMPlacesFromRoom()
        }
    }

    // --alogData----------------------
    private val _alogData = MutableLiveData<ALog>()
    val alogData: LiveData<ALog>
        get() = _alogData

    private val _alogDataS = MutableLiveData<List<ALog>>()
    val alogDataS: LiveData<List<ALog>>
        get() = _alogDataS

    fun addLogEntry(type: ALogType, logText: String?, lopDetail: String?) {
        val currentDate = Date()
        val formattedDateUTC = DateTimeUtils.formatDateTimeToUTC(Date())
        //   Log.d("Formatted Date (UTC)", formattedDateUTC)
        var myAlog: ALog = ALog(0, formattedDateUTC, type, logText, lopDetail)
        _alogData.postValue(myAlog)
        //   Log.d("ufe", "ALog Room:" + myAlog.toString())
        database.TickTraxDao.insertLogEntry((myAlog))
        //   Log.d("ufe", "ALog after db insert:" + myAlog.toString())
        getAllLogEntriesFromRoom()
    }

    fun addLogEntry(type: ALogType, logText: String?) {
        addLogEntry(type, logText, "rep " + logText)
    }

    private fun getAllLogEntriesFromRoom() {
        try {
            var allLogsEntries = database.TickTraxDao.getAllLogEntries()
            Log.d("ufe", "I read " + allLogsEntries.size + " ALog Records from ROOM")
            _alogDataS.postValue(allLogsEntries)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    suspend fun getCountOfLogEntries(): Int {
        return database.TickTraxDao.countLogEntries()
    }

    suspend fun deleteSmallestIdEntry() {
        while (getCountOfLogEntries() > ALog_ROOM_Max) {
            val smallestIdEntry = database.TickTraxDao.getSmallestALogIdEntry()
            smallestIdEntry?.let {
                database.TickTraxDao.deleteALogId(it)
            }
        }
    }
}


