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
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceExt
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocation
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocationDetail
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessOSMPlaceDetail
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
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
        }
    }


    // ----------------------------------------------------------------
    // OSMPlace
    // ----------------------------------------------------------------

    var _OSMPlace4ID = MutableLiveData<OSMPlace>()
    fun readOSMPlace4IdFromRoom(OSMId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _OSMPlace4ID.postValue(database.TickTraxDao.getOSMPlace4Id(OSMId))
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
        }
    }

    var _OSMPlace4LonLat = MutableLiveData<OSMPlace>()
    fun readOSMPlace4LonLat(lon: Double, lat: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _OSMPlace4LonLat.postValue(OSMGsonApi.apiGsonService.getOSMPlace(lat, lon))
            } catch (e: Exception) {
                // Behandeln Sie Fehler hier
                Log.e("ufe", e.toString())
            }
        }
    }

    private var _OSMLivePlace = MutableLiveData<OSMPlace>()
    val OSMLivePlace: LiveData<OSMPlace>
        get() = _OSMLivePlace

    var oldLat: Double = 0.0
    var oldLon: Double = 0.0
    var oldOSMPlace = OSMPlace(0L,0L,0.0,0.0)
    suspend fun getPlaceFromOSMandInsertOrUpdateRoom(lat: Double, lon: Double) {
        addLogEntry( ALogType.GEO, "check " + oldLat + " " + lat + "(" + (oldLat == lat).toString() + ")" + " / " + oldLon + " " + lon + "(" + (oldLon == lon).toString() + ")" )
        var newOSMPlace = OSMPlace(0L,0L,0.0,0.0)
        if (oldLat == lat && oldLon == lon) {
            addLogEntry(ALogType.GEO, "OSM skipped, loc not changed")
        } else
            try {
                newOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
                newOSMPlace.locAdded = Date()
                newOSMPlace.lastSeen = Date()
                _OSMLivePlace.postValue(newOSMPlace)
                addOrUpdateOSMPlaceDetail(newOSMPlace)
                addLogEntry(ALogType.GEO, "OSM Place added", newOSMPlace.toString())
                database.TickTraxDao.insertOSMPlace(newOSMPlace)
                oldLat = lat
                oldLon = lon
            } catch (e: Exception) {
                Log.e("ufe", "Error loading Data from API: $e")
            }
        oldOSMPlace = newOSMPlace
        logDebug("ufe", "before update detail for "+oldOSMPlace.toString())
        getAllOSMPlacesFromRoom()
        getAllOSMPlaceExtFromRoom()
    }

    private var _OSMPlaceS = MutableLiveData<List<OSMPlace>>()
    val OSMPlaceS: LiveData<List<OSMPlace>>
        get() = _OSMPlaceS

    fun getAllOSMPlacesFromRoom() {
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                var allPlaces = database.TickTraxDao.getAllOSMPlaces()
                logDebug("ufe", "I read " + allPlaces.size + " OSMPlaces from ROOM")
                _OSMPlaceS.postValue(allPlaces)
            } catch (e: Exception) {
                Log.e("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    // ----------------------------------------------------------------
    // OSMPlacedetails
    // ----------------------------------------------------------------
    private val _OSMPlaceDetail = MutableLiveData<OSMPlaceDetail>()
    val OSMPlaceDetail: LiveData<OSMPlaceDetail>
        get() = _OSMPlaceDetail

    private var _OSMPlaceDetailS = MutableLiveData<List<OSMPlaceDetail>>()
    val OSMPlaceDetailS: LiveData<List<OSMPlaceDetail>>
        get() = _OSMPlaceDetailS

    val _OSMPlaceDetailS4Id = MutableLiveData<List<OSMPlaceDetail>>()
    fun getAllOSMPlaceDetailFromRoom4Id(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allOSMPlaceDetail = database.TickTraxDao.getAallOSMPlaceDetail(id)
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _OSMPlaceDetailS4Id.postValue(allOSMPlaceDetail)
            } catch (e: Exception) {
                Log.e("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    var lastOSMPlaceDetail = OSMPlaceDetail(0)
    var lastOSMPlace = OSMPlace(0L,0L,0.0,0.0)
    fun addOrUpdateOSMPlaceDetail(currentOSMPlace: OSMPlace) {

        logDebug("ufe", "addOrUpdateOSMPlaceDetail called with " + currentOSMPlace)
        // for the case we need a new OSMPlace detail
        val currentOSMPlaceDetail = OSMPlaceDetail(
            0L,
            currentOSMPlace.OSMPlaceId,
            Date(),
            Date(),
            0,
        )

        // oldOSMPlacedetail is null --> first start
        if (lastOSMPlaceDetail.OSMPlaceDetailId == 0L) {
            // then we have no id we can try to load
            // so lets load the last entry we made to room details table and take that as old
            var bla = database.TickTraxDao.getAOSMPlaceDetailLatestEntry()
            if (bla != null) {
                lastOSMPlaceDetail = bla
            } else
                lastOSMPlaceDetail = currentOSMPlaceDetail
        }

        // lastOSMPlace is null --> first start
        if (lastOSMPlace == null) {
            lastOSMPlace = currentOSMPlace
        }

        var NewOrUpdatedOSMPlaceDetail: OSMPlaceDetail
        // is the current OSMPlace the OSMPlace from the last detail?
        if (currentOSMPlace.OSMPlaceId == lastOSMPlaceDetail.OSMPlaceId) {
            //yes it is, let's update it
            // we want to update the alredy existing entry we we need the id
            currentOSMPlaceDetail.OSMPlaceDetailId = lastOSMPlaceDetail.OSMPlaceDetailId
            // do the miracle around the OSMPlaces
            NewOrUpdatedOSMPlaceDetail = ProcessOSMPlaceDetail(
                lastOSMPlace,
                currentOSMPlace,
                lastOSMPlaceDetail,
                currentOSMPlaceDetail
            )
        } else {
            // no, start over
            NewOrUpdatedOSMPlaceDetail = currentOSMPlaceDetail
        }

        _OSMPlaceDetail.postValue(NewOrUpdatedOSMPlaceDetail)

        try {
            logDebug(
                "ufe",
                "OSMPlace insert or update "
                        + NewOrUpdatedOSMPlaceDetail.toString()
            )
            database.TickTraxDao.insertOrUpdateOSMPlaceDetail((NewOrUpdatedOSMPlaceDetail))
            // OMG, I don't get the OSMPlaceDetailID for saving it for the old one, so just read it again
            NewOrUpdatedOSMPlaceDetail = database.TickTraxDao.getAOSMPlaceDetailLatestEntry()!!
        } catch (e: Exception) {
            Log.e("ufe", e.toString())
        }
        getAllOSMPlaceExtFromRoom()
        lastOSMPlaceDetail = NewOrUpdatedOSMPlaceDetail
        lastOSMPlace = currentOSMPlace
    }

    // ----------------------------------------------------------------
    // OSMPlaceExt
    // ----------------------------------------------------------------
    private val _OSMPlaceExt = MutableLiveData<OSMPlaceExt>()
    val OSMPlaceExt: LiveData<OSMPlaceExt>
        get() = _OSMPlaceExt

    private var _OSMPlaceExtS = MutableLiveData<List<OSMPlaceExt>>()
    val OSMPlaceExtS: LiveData<List<OSMPlaceExt>>
        get() = _OSMPlaceExtS

    fun getAllOSMPlaceExtFromRoom() {
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                var allOSMPlaceExt = database.TickTraxDao.getAllOSMPlaceExt()
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _OSMPlaceExtS.postValue(allOSMPlaceExt)
            } catch (e: Exception) {
                Log.e("ufe", "Error loading Data from ROOM: $e")
            }
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

    val _ttLocationDetailS4Id = MutableLiveData<List<TTLocationDetail>>()
    fun getAllLocationDetailFromRoom4Id(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allLocationDetail = database.TickTraxDao.getAllLocationDetail(id)
                //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
                _ttLocationDetailS4Id.postValue(allLocationDetail)
            } catch (e: Exception) {
                Log.e("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

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
            currentLocationDetail.LocationDetailId = lastLocationDetail.LocationDetailId
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
            ttNewOrUpdatedLocationDetail =
                database.TickTraxDao.getAlocationDetailLatestEntry()!!
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
        getAllLocationsFromRoom()
        _ttLocation.postValue(newLocation)
        // ok, this is a live upate so update also OSM Place
        CoroutineScope(Dispatchers.IO).launch()
        {
            try {
                getPlaceFromOSMandInsertOrUpdateRoom(newLocation.lat, newLocation.lon)
            } catch (e: Exception) {
                Log.e("ufe", e.toString())
            }
            getAllOSMPlacesFromRoom()
            getAllLocationsFromRoom()
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



