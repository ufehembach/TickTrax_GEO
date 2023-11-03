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
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocation
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocationDetail
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
        logDebug("ufe ", "TickTraxAppRepository -> INIT")

    }

    public fun setParas(pOSMGsonApi: OSMGsonApi, pDatabase: TickTraxDB) {
        oSMGsonApi = pOSMGsonApi
        database = pDatabase
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Führen Sie hier asynchrone Aufgaben aus, z.B. eine Netzwerkanfrage
                logDebug("ufe", "Read Room")
                getAllLocationsFromRoom()
                logDebug("ufe", "I read " + (_ttLocationS.value?.size) + " locations")
                getAllOSMPlacesFromRoom()
                logDebug("ufe", "I read " + _OSMPlaceS.value?.size + " places")
                getAllTTAggregationsFromRoom()
                logDebug("ufe", "I read " + _TTAggregationS.value?.size + " aggregations")
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
        }
    }

    // ----------------------------------------------------------------
    // TTAggregation
    // ----------------------------------------------------------------
    private var _TTAggregation = MutableLiveData<TTAggregation>()
    val TTAggregation: LiveData<TTAggregation>
        get() = _TTAggregation

    private var _TTAggregationS = MutableLiveData<List<TTAggregation>>()
    val TTAggregationS: LiveData<List<TTAggregation>>
        get() = _TTAggregationS

    suspend fun getAllTTAggregationsFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllTTAggregations()
            //  logDebug("ufe", "I read " + allPlaces.size + " TTAggregations from ROOM")
            _TTAggregationS.postValue(allPlaces)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    // ----------------------------------------------------------------
    // OSMPlace
    // ----------------------------------------------------------------
    private var _OSMPlace = MutableLiveData<OSMPlace>()
    val OSMPlace: LiveData<OSMPlace>
        get() = _OSMPlace

    var oldLat: Double = 0.0
    var oldLon: Double = 0.0
    suspend fun getPlaceFromOSM(lat: Double, lon: Double) {

//        addLogEntry(
//            ALogType.GEO, "check " +
//                    oldLat + " " + lat +
//                    "(" + (oldLat == lat).toString() + ")" +
//                    " / " +
//                    oldLon + " " + lon +
//                    "(" + (oldLon == lon).toString() + ")"
//        )
        if (oldLat == lat && oldLon == lon) {
            addLogEntry(ALogType.GEO, "OSM skipped, loc not changed")
        } else
            try {
                //  logDebug("ufe", "load Data from API")
                val gsonOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
                //  logDebug("ufe", "OSM Data from API ")
                gsonOSMPlace.firstSeen = DateTimeUtils.formatDateTimeToUTC(Date())
                gsonOSMPlace.lastSeen = DateTimeUtils.formatDateTimeToUTC(Date())
                gsonOSMPlace.noOfSights = 1
                _OSMPlace.postValue(gsonOSMPlace)
                addLogEntry(ALogType.GEO, "OSM Place added", gsonOSMPlace.toString())
                database.TickTraxDao.insertOSMPlace(gsonOSMPlace)
                oldLat = lat
                oldLon = lon
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
            //  logDebug("ufe", "I read " + allPlaces.size + " OSMPlaces from ROOM")
            _OSMPlaceS.postValue(allPlaces)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }


    // ----------------------------------------------------------------
    // locationdetails
    // ----------------------------------------------------------------
    private val _ttLocationDetail = MutableLiveData<TTLocationDetail>()
    val ttLocationDetail: LiveData<TTLocationDetail>
        get() = _ttLocationDetail

    private var _ttLocationDetailS = MutableLiveData<List<TTLocationDetail>>()
    val ttLocationDetailS: LiveData<List<TTLocationDetail>>
        get() = _ttLocationDetailS

    var lastLocationDetail = TTLocationDetail()
    var lastLocation = TTLocation()
    fun addOrUpdateLocationDetail(currentLocation: TTLocation) {

        logDebug("ufe", "addOrUpdateLocationDetail called with " + currentLocation)
        // for the case we need a new location detail
        val currentLocationDetail = TTLocationDetail(
            0L,
            currentLocation.LocationId,
            Date(),
            Date(),
            0,
        )

        // oldlocationdetail is null --> first start
        if (lastLocationDetail.LocationDetailId == 0L) {
            // then we have no id we can try to load
            // so lets load the last entry we made to room details table and take that as old
            var bla = database.TickTraxDao.getAlocationDetailLatestEntry()
            if (bla != null) {
                lastLocationDetail = bla
            } else
                lastLocationDetail = currentLocationDetail
        }

        // lastlocation is null --> first start
        if (lastLocation == null) {
            lastLocation = currentLocation
        }

        var ttNewOrUpdatedLocationDetail: TTLocationDetail
        // is the current location the location from the last detail?
        if (currentLocation.LocationId == lastLocationDetail.LocationId) {
            //yes it is, let's update it
            // we want to update the alredy existing entry we we need the id
            currentLocationDetail.LocationDetailId = lastLocationDetail.LocationId
            // do the miracle around the locations
            ttNewOrUpdatedLocationDetail = ProcessLocationDetail(
                lastLocation,
                currentLocation,
                lastLocationDetail,
                currentLocationDetail
            )
        } else {
            // no, start over
            ttNewOrUpdatedLocationDetail = currentLocationDetail
        }

        _ttLocationDetail.postValue(ttNewOrUpdatedLocationDetail)
        try {
            logDebug(
                "ufe",
                "location insert or update "
                        + ttNewOrUpdatedLocationDetail.toString()
            )
            database.TickTraxDao.insertOrUpdateLocationDetail((ttNewOrUpdatedLocationDetail))
            // OMG, I don't get the LocationDetailID for saving it for the old one, so just read it again
            ttNewOrUpdatedLocationDetail = database.TickTraxDao.getAlocationDetailLatestEntry()!!
        } catch (e: Exception) {
            Log.e("ufe", e.toString())
        }
        lastLocationDetail = ttNewOrUpdatedLocationDetail
        lastLocation = currentLocation
    }

    // ----------------------------------------------------------------
    // ttLocationExt
    // ----------------------------------------------------------------
    private val _ttLocationExt = MutableLiveData<TTLocationExt>()
    val ttLocationExt: LiveData<TTLocationExt>
        get() = _ttLocationExt

    private var _ttLocationExtS = MutableLiveData<List<TTLocationExt>>()
    val ttLocationExtS: LiveData<List<TTLocationExt>>
        get() = _ttLocationExtS

    suspend fun getAllLocationExtFromRoom() {
        try {
            var allLocationExt = database.TickTraxDao.getAllLocationExt()
            //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
            _ttLocationExtS.postValue(allLocationExt)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    // ----------------------------------------------------------------
    // ttLocationData
    // ----------------------------------------------------------------
    private val _ttLocation = MutableLiveData<TTLocation>()
    val ttLocation: LiveData<TTLocation>
        get() = _ttLocation

    private var _ttLocationS = MutableLiveData<List<TTLocation>>()
    val ttLocationS: LiveData<List<TTLocation>>
        get() = _ttLocationS

    suspend fun getAllLocationsFromRoom() {
        try {
            var allLocations = database.TickTraxDao.getAllLocations()
            //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
            _ttLocationS.postValue(allLocations)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from ROOM: $e")
        }
    }

    var ttOldLocation = TTLocation()
    suspend fun addLocation(myLocation: Location) {
        val currentDate = Date()
        val myHash = GEOHash(myLocation)
        val newLocation = TTLocation(
            myHash,
            myLocation.longitude,
            myLocation.latitude,
            myLocation.altitude,
            Date(),
            Date(),
            0.0
        )
        // oldlocation is null --> first start
        if (ttOldLocation.LocationId == 0L) {
            // then we have no id we can try to load
            // so lets load the last entry we made to room details table and take that as old
            var bla = database.TickTraxDao.getAlocationLatestEntry()
            if (bla != null) {
                ttOldLocation = bla
            } else
                ttOldLocation = newLocation
        }

        var newOrUpdatedLocation: TTLocation
        // is the current location the new location?
        if (ttOldLocation.LocationId == newLocation.LocationId) {
            //yes it is, let's update it
            newOrUpdatedLocation = ProcessLocation(ttOldLocation, newLocation)
        } else {
            // no, start over
            newOrUpdatedLocation = newLocation
        }

        logDebug("ufe", "location " + newOrUpdatedLocation.toString())
        database.TickTraxDao.insertOrUpdateLocation((newOrUpdatedLocation))
        addOrUpdateLocationDetail(newLocation)
        ttOldLocation = newLocation
        getAllLocationExtFromRoom()
        //TODO was mache ich hier?
        CoroutineScope(Dispatchers.IO).launch()
        {
            try {
                getPlaceFromOSM(newLocation.lat, newLocation.lon)
            } catch (e: Exception) {
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

    var oldLogText: String? = ""
    fun addLogEntry(type: ALogType, logText: String?, lopDetail: String?) {

        if (oldLogText == logText) {
            return
        }

        val currentDate = Date()
        val formattedDateUTC = DateTimeUtils.formatDateTimeToUTC(Date())
        //   logDebug("Formatted Date (UTC)", formattedDateUTC)
        var myAlog: ALog = ALog(0, formattedDateUTC, type, logText, lopDetail)
        _alogData.postValue(myAlog)
        //   logDebug("ufe", "ALog Room:" + myAlog.toString())
        database.TickTraxDao.insertLogEntry((myAlog))
        //   logDebug("ufe", "ALog after db insert:" + myAlog.toString())
        // Starten Sie eine Coroutine auf dem Dispatchers.IO-Thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Führen Sie hier asynchrone Aufgaben aus, z.B. eine Netzwerkanfrage
                deleteSmallestIdEntry()
                getAllLogEntriesFromRoom()
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
            getAllOSMPlacesFromRoom()
            oldLogText = logText
        }
    }

    fun addLogEntry(type: ALogType, logText: String?) {
        addLogEntry(type, logText, "rep " + logText)
    }

    private fun getAllLogEntriesFromRoom() {
        try {
            var allLogsEntries = database.TickTraxDao.getAllLogEntries()
//            logDebug("ufe", "I read " + allLogsEntries.size + " ALog Records from ROOM")
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
            //   logDebug(
            //       "ufe ",
            //       "we have " + getCountOfLogEntries() + " entries in room, threshold is " + ALog_ROOM_Max
            //   )
            val smallestIdEntry = database.TickTraxDao.getSmallestALogIdEntry()
            smallestIdEntry?.let {
                //               logDebug("ufe ", "delete entry ")
                database.TickTraxDao.deleteALogId(it)
            }
        }
    }
}



