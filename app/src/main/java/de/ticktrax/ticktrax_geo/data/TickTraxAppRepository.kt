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
import de.ticktrax.ticktrax_geo.myTools.logError
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocation
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessLocationDetail
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessOSMPlace
import de.ticktrax.ticktrax_geo.processData.TTProcess.Companion.ProcessOSMPlaceDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    public fun setParas(pOSMGsonApi: OSMGsonApi, pDatabase: TickTraxDB) {
        oSMGsonApi = pOSMGsonApi
        database = pDatabase
        //readInit()
    }

    // ----------------------------------------------------------------
    init {
        logDebug("ufe ", "TickTraxAppRepository -> INIT")
        //readInit()
    }

    // ----------------------------------------------------------------
    fun readInit() {
        logDebug("ufe ", "TickTraxAppRepository -> readInit")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                logDebug("ufe", "Read Room")
                runBlocking {
                    getAllOSMPlacesFromRoom()
                    getAllOSMPlacesExtFromRoom()
                    getAllOSMPlacesDetailFromRoom()

                    getAllLocationsFromRoom()
                    getAllLocationsExtFromRoom()
                    getAllLocationsDetailFromRoom()

                    getAllLogEntriesFromRoom()
                }
//                addLogEntry(
//                    ALogType.INFO,
//                    "read " +
//                            OSMPlaceS.value?.size.toString() + "/" +
//                            OSMPlaceExtS.value?.size.toString() + "/" +
//                            OSMPlaceDetailS.value?.size.toString() + "/" +
//
//                            locationS.value?.size.toString() + "/" +
//                            locationExtS.value?.size.toString() + "/" +
//                            locationDetailS.value?.size.toString() + "/" +
//
//                            aLogDataS.value?.size.toString()
//
//                )
                logDebug(
                    "ufe",
                    "read " +
                            OSMPlaceS.value?.size.toString() + "/" +
                            OSMPlaceExtS.value?.size.toString() + "/" +
                            OSMPlaceDetailS.value?.size.toString() + "/" +

                            locationS.value?.size.toString() + "/" +
                            locationExtS.value?.size.toString() + "/" +
                            locationDetailS.value?.size.toString() + "/" +

                            aLogDataS.value?.size.toString()
                )
            } catch (e: Exception) {
                Log.e("ufe", e.toString())
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------
           _       _           _                   _       _     _
      __ _| | ___ | |__   __ _| | __   ____ _ _ __(_) __ _| |__ | | ___  ___
     / _` | |/ _ \| '_ \ / _` | | \ \ / / _` | '__| |/ _` | '_ \| |/ _ \/ __|
    | (_| | | (_) | |_) | (_| | |  \ V / (_| | |  | | (_| | |_) | |  __/\__ \
     \__, |_|\___/|_.__/ \__,_|_|   \_/ \__,_|_|  |_|\__,_|_.__/|_|\___||___/
     |___/
      -----------------------------------------------------------------------------------------------
     */

    // ----------------------------------------------------------------
    var oldLat: Double = 0.0
    var oldLon: Double = 0.0

    // ----------------------------------------------------------------
    var lastOSMPlace = OSMPlace(0L, 0L, 0.0, 0.0)
    var lastOSMPlaceDetail = OSMPlaceDetail(0)

    // ----------------------------------------------------------------
    private var _newOSMPlace = MutableLiveData<OSMPlace>()
    val newOSMPlace: LiveData<OSMPlace>
        get() = _newOSMPlace

    // ----------------------------------------------------------------
    var lastLocation = TTLocation()
    var lastLocationDetail = TTLocationDetail()

    // one
    // ----------------------------------------------------------------
    private var _OSMPlace = MutableLiveData<OSMPlace>()
    val OSMPlace: LiveData<OSMPlace>
        get() = _OSMPlace

    // ----------------------------------------------------------------
    private val _OSMPlaceExt = MutableLiveData<OSMPlaceExt>()
    val OSMPlaceExt: LiveData<OSMPlaceExt>
        get() = _OSMPlaceExt

    // ----------------------------------------------------------------
    private val _OSMPlaceDetail = MutableLiveData<OSMPlaceDetail>()
    val OSMPlaceDetail: LiveData<OSMPlaceDetail>
        get() = _OSMPlaceDetail

    // ----------------------------------------------------------------
    private val _location = MutableLiveData<TTLocation>()
    val location: LiveData<TTLocation>
        get() = _location

    // ---------------------------------------------------------------
    private val _locationExt = MutableLiveData<TTLocationExt>()
    val locationExt: LiveData<TTLocationExt>
        get() = _locationExt

    // ----------------------------------------------------------------
    private val _locationDetail = MutableLiveData<TTLocationDetail>()
    val locationDetail: LiveData<TTLocationDetail>
        get() = _locationDetail

    // all
    // ----------------------------------------------------------------
    private var _OSMPlaceS = MutableLiveData<List<OSMPlace>>()
    val OSMPlaceS: LiveData<List<OSMPlace>>
        get() = _OSMPlaceS

    // ----------------------------------------------------------------
    private var _OSMPlaceExtS = MutableLiveData<List<OSMPlaceExt>>()
    val OSMPlaceExtS: LiveData<List<OSMPlaceExt>>
        get() = _OSMPlaceExtS

    // ----------------------------------------------------------------
    private var _OSMPlaceDetailS = MutableLiveData<List<OSMPlaceDetail>>()
    val OSMPlaceDetailS: LiveData<List<OSMPlaceDetail>>
        get() = _OSMPlaceDetailS

    // ----------------------------------------------------------------
    private var _locationS = MutableLiveData<List<TTLocation>>()
    val locationS: LiveData<List<TTLocation>>
        get() = _locationS

    // ----------------------------------------------------------------
    private var _locationExtS = MutableLiveData<List<TTLocationExt>>()
    val locationExtS: LiveData<List<TTLocationExt>>
        get() = _locationExtS

    // ----------------------------------------------------------------
    private var _locationDetailS = MutableLiveData<List<TTLocationDetail>>()
    val locationDetailS: LiveData<List<TTLocationDetail>>
        get() = _locationDetailS

    // ----------------------------------------------------------------
    var _OSMPlace4ID = MutableLiveData<OSMPlace>()
    var _OSMPlace4LonLat = MutableLiveData<OSMPlace>()
    val _OSMPlaceDetailS4Id = MutableLiveData<List<OSMPlaceDetail>>()
    val _locationDetailS4Id = MutableLiveData<List<TTLocationDetail>>()

    var _readInit: Boolean = false
    suspend fun addLatLonAlt(lat: Double, lon: Double, alt: Double) {
        // ----------------------------------------------------------------
        // startup stuff
        // don't know when and how the apprepository will be initialzied, but this seems to work
        if (!_readInit) {
            logDebug("ufe", "AppRep: _readinit " + _readInit)
            readInit();
            _readInit = true
            logDebug("ufe", "AppRep: _readinit " + _readInit)
        }
        if (lastOSMPlace == null && lastLocation == null && lastOSMPlaceDetail == null && lastLocationDetail == null) {
            // app started
            // lastOSMPlace = database.TickTraxDao.getLastOSMPlace()
            lastOSMPlace = OSMPlace(0L, 0L, 0.0, 0.0)
            // lastOSMPlaceDetail = database.TickTraxDao.getLastOSMPlaceDetail()
            lastOSMPlaceDetail = OSMPlaceDetail(0L, 0L)
            //lastLocation = database.TickTraxDao.getLastLocation()
            lastLocation = TTLocation()
            //lastLocationDetail = database.TickTraxDao.getLastLocationDetail()
            lastLocationDetail = TTLocationDetail()
        }
        if (_location.value != null) {
            lastLocation = _location.value!!
        }
        if (_newOSMPlace.value != null) {
            lastOSMPlace = _OSMPlace.value!!
        }
        if (_locationDetail.value != null) {
            lastLocationDetail = _locationDetail.value!!
        }
        if (_OSMPlaceDetail.value != null) {
            lastOSMPlaceDetail = _OSMPlaceDetail.value!!
        }
        addLogEntry(
            ALogType.INFO,
            ": " + lat.toString() + "/" + lon.toString() + "/" + alt.toString()
        )
        if (oldLat != lat && oldLon != lon)
            getAPlaceFromOSMandAdd2DB(lat, lon, alt)
        else {
            addLogEntry(
                ALogType.INFO,
                "old location: " + lat.toString() + "/" + lon.toString() + "/" + alt.toString()
            )
            lastLocation.lastSeen = Date()
            lastOSMPlace.lastSeen = Date()
            lastLocationDetail.lastSeen = Date()
            lastOSMPlaceDetail.lastSeen = Date()
            updatePlace(lastOSMPlace)
            updatePlaceDetail(lastOSMPlaceDetail)
            updateLocation(lastLocation)
            updateLocationDetail(lastLocationDetail)

        }
        addLogEntry(
            ALogType.GEO,
            "getAOSMPlaceExtBasedOnID " + OSMPlace.toString()
        )
        if(OSMPlace.value != null)
            getAOSMPlaceExtBasedOnID(OSMPlace.value!!.OSMPlaceId)
        addLogEntry(
            ALogType.GEO,
            "set OLD Variables  " + OSMPlace.toString()
        )
        oldLat = lat
        oldLon = lon
    }

    /*-----------------------------------------------------------------------------------------------
           _                           _          __  __
     _ __ | | __ _  ___ ___  ___   ___| |_ _   _ / _|/ _|
    | '_ \| |/ _` |/ __/ _ \/ __| / __| __| | | | |_| |_
    | |_) | | (_| | (_|  __/\__ \ \__ \ |_| |_| |  _|  _|
    | .__/|_|\__,_|\___\___||___/ |___/\__|\__,_|_| |_|
    |_|
     -----------------------------------------------------------------------------------------------
    */
    suspend fun getAPlaceFromOSMandAdd2DB(lat: Double, lon: Double, alt: Double) {
        var nOSMPlace = OSMPlace(0L, 0L, 0.0, 0.0)
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    nOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
                    nOSMPlace.firstSeen = Date()
                    nOSMPlace.lastSeen = Date()
                    addLogEntry(
                        ALogType.GEO,
                        "OSM Place added " + nOSMPlace.OSMPlaceId,
                        nOSMPlace.toString()
                    )
                    database.TickTraxDao.insertOSMPlace(nOSMPlace)
                    _OSMPlace.postValue( nOSMPlace)

                    getAllOSMPlacesFromRoom()
                    getAllOSMPlacesExtFromRoom()
                    createAOSMPlaceDetail(nOSMPlace.OSMPlaceId)
                    val newLocation = TTLocation(
                        GEOHash(lat, lon),
                        lon,
                        lat,
                        alt,
                        nOSMPlace.OSMPlaceId,
                        Date(),
                        Date(),
                        0.0
                    )
                    addALocations2Room(newLocation)
                    addLogEntry(
                        ALogType.GEO,
                        "getAOSMPlaceExtBasedOnID " + nOSMPlace.OSMPlaceId,
                        nOSMPlace.toString()
                    )
                    getAOSMPlaceExtBasedOnID(nOSMPlace.OSMPlaceId)
                } catch (e: Exception) {
                    logError("ufe", "Error loading Data from API: $e")
                }
            }
        }
    }

    suspend fun updatePlace(place: OSMPlace) {
        try {
            database.TickTraxDao.updateOSMPlace(place)
            _OSMPlace.postValue(place)

            getAllOSMPlacesFromRoom()
            getAllOSMPlacesExtFromRoom()
            addLogEntry(ALogType.GEO, "Place updated")
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from API: $e")
        }
    }

    fun getAllOSMPlacesFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllOSMPlaces()
            // logDebug("ufe", "I read " + allPlaces.size + " OSMPlaces from ROOM")
            _OSMPlaceS.postValue(allPlaces)
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
    }

        suspend fun getAOSMPlaceExtBasedOnID(OSMPlaceID: Long) {
            logDebug("ufe", "try to get " + OSMPlaceID)
            addLogEntry(
                ALogType.GEO,
                "OSM PlaceExt get  " + OSMPlaceID
            )
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    var osmPlaceExt = database.TickTraxDao.getOSMPlaceExtById(OSMPlaceID)
                    addLogEntry(
                        ALogType.GEO,
                        "OSM PlaceExt set  " + osmPlaceExt
                    )
                    _OSMPlaceExt.postValue(osmPlaceExt)
                } catch (e: Exception) {
                    logError("ufe", "Error loading Data from ROOM: $e")
                }
            }
    }

    /*---------------------------------------------------------------------------------------------
           _                           _      _        _ _       _          __  __
     _ __ | | __ _  ___ ___  ___    __| | ___| |_ __ _(_) |  ___| |_ _   _ / _|/ _|
    | '_ \| |/ _` |/ __/ _ \/ __|  / _` |/ _ \ __/ _` | | | / __| __| | | | |_| |_
    | |_) | | (_| | (_|  __/\__ \ | (_| |  __/ || (_| | | | \__ \ |_| |_| |  _|  _|
    | .__/|_|\__,_|\___\___||___/  \__,_|\___|\__\__,_|_|_| |___/\__|\__,_|_| |_|
          |_|
    -----------------------------------------------------------------------------------------------
    */
    fun createAOSMPlaceDetail(osmPlaceId: Long) {
        var OSMPlaceDetail = OSMPlaceDetail(0L, osmPlaceId, Date(), Date(), 0L, 0.0)

        try {
            logDebug(
                "ufe",
                "OSMPlace insert"
                        + OSMPlaceDetail.toString()
            )
            val ID = database.TickTraxDao.insertOSMPlaceDetail(OSMPlaceDetail)
            OSMPlaceDetail.OSMPlaceId = ID
            _OSMPlaceDetail.value = OSMPlaceDetail

            getAllOSMPlacesDetailFromRoom()
            addLogEntry(
                ALogType.GEO,
                "OSM Place Detail added  " + ID + " for " + OSMPlaceDetail.OSMPlaceId
            )
        } catch (e: Exception) {
            Log.e("ufe", e.toString())
        }
    }

    fun updatePlaceDetail(placeDetail: OSMPlaceDetail) {
        addLogEntry(
            ALogType.GEO,
            "try OSM Place Detail updated  "
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.TickTraxDao.updateOSMPlaceDetail(placeDetail)
                _OSMPlaceDetail.value = placeDetail

                getAllOSMPlacesDetailFromRoom()
                addLogEntry(
                    ALogType.GEO,
                    "OSM Place Detail updated  "
                )
            } catch (e: Exception) {
                Log.e("ufe", e.toString())
            }
        }
    }

    fun getAllOSMPlacesDetailFromRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allOSMPlaceDetailS = database.TickTraxDao.getAllOSMPlaceDetails()
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _OSMPlaceDetailS.postValue(allOSMPlaceDetailS)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }


    /*---------------------------------------------------------------------------------------------
       _                 _   _                   _          __  __
      | | ___   ___ __ _| |_(_) ___  _ __    ___| |_ _   _ / _|/ _|
      | |/ _ \ / __/ _` | __| |/ _ \| '_ \  / __| __| | | | |_| |_
      | | (_) | (_| (_| | |_| | (_) | | | | \__ \ |_| |_| |  _|  _|
      |_|\___/ \___\__,_|\__|_|\___/|_| |_| |___/\__|\__,_|_| |_|

       -----------------------------------------------------------------------------------------------
       */

    fun getAllLocationsFromRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allLocations = database.TickTraxDao.getAllLocations()
                //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
                _locationS.postValue(allLocations)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    fun addALocations2Room(newLoc: TTLocation) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ID = database.TickTraxDao.insertLocation(newLoc)
                newLoc.LocationId = ID
                _location.value = newLoc

                getAllLocationsFromRoom()
                getAllLocationsExtFromRoom()

                addLogEntry(
                    ALogType.GEO,
                    "Location added " + newLoc.LocationId + " for " + newLoc.OSMPlaceId
                )

                val newLocationDetail = TTLocationDetail(0L, newLoc.LocationId)
                createALocationDetail(newLocationDetail)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    fun updateLocation(newLoc: TTLocation) {
        addLogEntry(
            ALogType.GEO,
            "try Location update " + newLoc.LocationId + " for " + newLoc.OSMPlaceId
        )
        try {
            database.TickTraxDao.insertLocation(newLoc)
            _location.postValue(newLoc)

            getAllLocationsFromRoom()
            getAllLocationsExtFromRoom()

            addLogEntry(
                ALogType.GEO,
                "Location update " + newLoc.LocationId + " for " + newLoc.OSMPlaceId
            )
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
    }


    /*---------------------------------------------------------------------------------------------
           _                                 _                 _          _
     _ __ | | __ _  ___ ___  ___    _____  _| |_ ___ _ __   __| | ___  __| |
    | '_ \| |/ _` |/ __/ _ \/ __|  / _ \ \/ / __/ _ \ '_ \ / _` |/ _ \/ _` |
    | |_) | | (_| | (_|  __/\__ \ |  __/>  <| ||  __/ | | | (_| |  __/ (_| |
    | .__/|_|\__,_|\___\___||___/  \___/_/\_\\__\___|_| |_|\__,_|\___|\__,_|
    |_|
    -----------------------------------------------------------------------------------------------
    */
    fun getAllOSMPlacesExtFromRoom() {
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                var allOSMPlaceExt = database.TickTraxDao.getAllOSMPlaceExt()
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _OSMPlaceExtS.postValue(allOSMPlaceExt)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    /*---------------------------------------------------------------------------------------------
    _                 _   _                   _      _        _ _
    | | ___   ___ __ _| |_(_) ___  _ __     __| | ___| |_ __ _(_) |___
    | |/ _ \ / __/ _` | __| |/ _ \| '_ \   / _` |/ _ \ __/ _` | | / __|
    | | (_) | (_| (_| | |_| | (_) | | | | | (_| |  __/ || (_| | | \__ \
    |_|\___/ \___\__,_|\__|_|\___/|_| |_|  \__,_|\___|\__\__,_|_|_|___/
    -----------------------------------------------------------------------------------------------
    */
    fun createALocationDetail(locationDetail: TTLocationDetail) {
        try {
            logDebug(
                "ufe",
                "location insert"
                        + locationDetail.toString()
            )
            val ID = database.TickTraxDao.insertLocationDetail(locationDetail)
            locationDetail.LocationDetailId = ID
            _locationDetail.value = locationDetail

            getAllLocationsDetailFromRoom()
            addLogEntry(
                ALogType.GEO,
                "Location Detail added  " + locationDetail.LocationDetailId + " for " + locationDetail.LocationId
            )
        } catch (e: Exception) {
            logError("ufe", e.toString())
        }
    }

    fun updateLocationDetail(locationDetail: TTLocationDetail) {
        addLogEntry(
            ALogType.GEO,
            "try Location Detail update "
        )
        try {
            database.TickTraxDao.updateLocationDetail(locationDetail)
            _locationDetail.postValue(locationDetail)
            getAllLocationsDetailFromRoom()
            addLogEntry(
                ALogType.GEO,
                "Location Detail update "
            )
        } catch (e: Exception) {
            logError("ufe", e.toString())
        }
    }

    fun getAllLocationsDetailFromRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allLocationDetailS = database.TickTraxDao.getAllLocationDetails()
                //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
                _locationDetailS.postValue(allLocationDetailS)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }


    fun getAllLocationDetailFromRoom4Id(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allLocationDetail = database.TickTraxDao.getAllLocationDetail(id)
                //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
                _locationDetailS4Id.postValue(allLocationDetail)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    /*---------------------------------------------------------------------------------------------
    _                 _   _                         _                 _          _
    | | ___   ___ __ _| |_(_) ___  _ __     _____  _| |_ ___ _ __   __| | ___  __| |
    | |/ _ \ / __/ _` | __| |/ _ \| '_ \   / _ \ \/ / __/ _ \ '_ \ / _` |/ _ \/ _` |
    | | (_) | (_| (_| | |_| | (_) | | | | |  __/>  <| ||  __/ | | | (_| |  __/ (_| |
    |_|\___/ \___\__,_|\__|_|\___/|_| |_|  \___/_/\_\\__\___|_| |_|\__,_|\___|\__,_|
    -----------------------------------------------------------------------------------------------
    */

    fun getAllLocationsExtFromRoom() {
        try {
            var allLocationExt = database.TickTraxDao.getAllLocationExt()
            //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
            _locationExtS.postValue(allLocationExt)
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
    }


    /*---------------------------------------------------------------------------------------------
          _
     __ _| |    ___   __ _
    / _` | |   / _ \ / _` |
    | (_| | |__| (_) | (_| |
    \__,_|_____\___/ \__, |
                     |___/
    -----------------------------------------------------------------------------------------------
    */
// --alogData----------------------
    private val _aLogData = MutableLiveData<ALog>()
    val aLogData: LiveData<ALog>
        get() = _aLogData

    private val _aLogDataS = MutableLiveData<List<ALog>>()
    val aLogDataS: LiveData<List<ALog>>
        get() = _aLogDataS

    var oldLogText: String? = ""
    fun getAllLogEntriesFromRoom() {
        try {
            var allLogsEntries = database.TickTraxDao.getAllLogEntries()
//            logDebug("ufe", "I read " + allLogsEntries.size + " ALog Records from ROOM")
            _aLogDataS.postValue(allLogsEntries)
            logDebug("ufe", "loading Data from ROOM: ${_aLogDataS.value?.size}")
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
    }

    fun addLogEntry(type: ALogType, logText: String?, lopDetail: String?) {


        if (oldLogText == logText) {
            return
        }

        val currentDate = Date()
        val formattedDateUTC = DateTimeUtils.formatDateTimeToUTC(Date())
        //   logDebug("Formatted Date (UTC)", formattedDateUTC)
        var myAlog: ALog = ALog(0, formattedDateUTC, type, logText, lopDetail)
        _aLogData.postValue(myAlog)
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
                logError("ufe", e.toString())
            }
            getAllOSMPlacesFromRoom()
            oldLogText = logText
        }
    }

    fun addLogEntry(type: ALogType, logText: String?) {
        addLogEntry(type, logText, "rep " + logText)
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


