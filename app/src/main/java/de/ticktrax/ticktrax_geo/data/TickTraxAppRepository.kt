package de.ticktrax.ticktrax_geo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.ALog_ROOM_Max
import de.ticktrax.ticktrax_geo.myTools.DateTimeUtils
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceExt
import de.ticktrax.ticktrax_geo.data.datamodels.Stats
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.myTools.GEOHash
import de.ticktrax.ticktrax_geo.myTools.createUniqueKey
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.myTools.logError
import de.ticktrax.ticktrax_geo.processData.TTProcess
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
                            osmPlaceS.value?.size.toString() + "/" +
                            osmPlaceExtS.value?.size.toString() + "/" +
                            osdmPlaceDetailS.value?.size.toString() + "/" +

                            locationS.value?.size.toString() + "/" +
                            locationExtS.value?.size.toString() + "/" +
                            locationDetailS.value?.size.toString() + "/" +

                            aLogDataS.value?.size.toString()
                )
            } catch (e: Exception) {
                logError("ufe", e.toString())
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
    private var _osmPlace = MutableLiveData<OSMPlace>()
    val osmPlace: LiveData<OSMPlace>
        get() = _osmPlace

    // ----------------------------------------------------------------
    private val _osmPlaceExt = MutableLiveData<OSMPlaceExt>()
    val osmPlaceExt: LiveData<OSMPlaceExt>
        get() = _osmPlaceExt

    // ----------------------------------------------------------------
    private val _osmPlaceDetail = MutableLiveData<OSMPlaceDetail>()
    val osmPlaceDetail: LiveData<OSMPlaceDetail>
        get() = _osmPlaceDetail

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
    private var _osmPlaceS = MutableLiveData<List<OSMPlace>>()
    val osmPlaceS: LiveData<List<OSMPlace>>
        get() = _osmPlaceS

    // ----------------------------------------------------------------
    private var _osmPlaceExtS = MutableLiveData<List<OSMPlaceExt>>()
    val osmPlaceExtS: LiveData<List<OSMPlaceExt>>
        get() = _osmPlaceExtS

    // ----------------------------------------------------------------
    private var _osmPlaceDetailS = MutableLiveData<List<OSMPlaceDetail>>()
    val osdmPlaceDetailS: LiveData<List<OSMPlaceDetail>>
        get() = _osmPlaceDetailS

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
    var _osmPlace4ID = MutableLiveData<OSMPlace>()
    var _osmPlace4LonLat = MutableLiveData<OSMPlace>()
    val _osmPlaceDetailS4Id = MutableLiveData<List<OSMPlaceDetail>>()
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
        //addLogEntry( ALogType.INFO, ": " + lat.toString() + "/" + lon.toString() + "/" + alt.toString() )

        // get stuff from OSM and set life data
        // manage placeDetails
        //      location
        //      and location details
        processPlaceOnLatLon(lat, lon, alt)
//        addLogEntry(
//            ALogType.GEO,
//            "OLD Variables ",
//            oldLat.toString() + "/" + oldLon.toString() + "\n"
//                    + "----Loc-----------\n"
//                    + location.value.toString() + "\n"
//                    + "----LocDetail------\n"
//                    + locationDetail.value.toString() + "\n"
//                    + "----Place----------\n"
//                    + osmPlace.value.toString() + "\n"
//                    + "----PlaceDetail----\n"
//                    + osmPlaceDetail.value.toString() + "\n"
//        )
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
    suspend fun processPlaceOnLatLon(lat: Double, lon: Double, alt: Double) {
        var nOSMPlace = OSMPlace(0L, 0L, 0.0, 0.0)
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    nOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
                    nOSMPlace.firstSeen = Date()
                    nOSMPlace.lastSeen = Date()
//                    addLogEntry(
//                        ALogType.GEO,
//                        "OSM Place " + nOSMPlace.osmPlaceId,
//                        nOSMPlace.toString()
//                    )
                    database.TickTraxDao.insertOSMPlace(nOSMPlace)
                    _osmPlace.postValue(nOSMPlace)

                    getAllOSMPlacesFromRoom()
                    getAllOSMPlacesExtFromRoom()
                    // save old for checking change
                    oldLat = lat
                    oldLon = lon
                    if (_newOSMPlace.value != null) {
                        lastOSMPlace = _osmPlace.value!!
                    }
                    // place done
                    // now place detail
                    processPlaceDetailOnOSMId(nOSMPlace.osmPlaceId)
                    // now location
                    // in location we do locationdetail
                    processLocationOnLatLon(nOSMPlace.osmPlaceId, lat, lon, alt)
                    getAOSMPlaceExtBasedOnID(nOSMPlace.osmPlaceId)
                    addLogEntry(
                        ALogType.GEO,
                        "OSM Place processed " + nOSMPlace.osmPlaceId
                    )
                } catch (e: Exception) {
                    logError("ufe", "Error loading Data from API: $e")
                }
            }
        }
    }

    suspend fun updatePlace(place: OSMPlace) {
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                val no = database.TickTraxDao.updateOSMPlace(place)
                _osmPlace.postValue(place)

                getAllOSMPlacesFromRoom()
                getAllOSMPlacesExtFromRoom()
                addLogEntry(
                    ALogType.GEO,
                    no.toString() + " Place updated " + _osmPlace.value?.lastSeen.toString()
                )
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from API: $e")
            }
            updateStats()
        }
    }

    fun getAllOSMPlacesFromRoom() {
        try {
            var allPlaces = database.TickTraxDao.getAllOSMPlaces()
            // logDebug("ufe", "I read " + allPlaces.size + " OSMPlaces from ROOM")
            _osmPlaceS.postValue(allPlaces)
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
        updateStats()
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
//                addLogEntry(
//                    ALogType.GEO,
//                    "OSM PlaceExt set on ID  ", osmPlaceExt.toString()
//                )
                _osmPlaceExt.postValue(osmPlaceExt)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
            updateStats()
        }
    }

    fun readOSMPlace4LonLat(lat: Double, lon: Double) {
        var nOSMPlace = OSMPlace(0L, 0L, 0.0, 0.0)
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                logDebug("ufe1", "load lat/lon " + lat.toString() + "/" + lon.toString())
                nOSMPlace = OSMGsonApi.apiGsonService.getOSMPlace(lat, lon)
                addLogEntry(
                    ALogType.GEO,
                    "OSM Place for lat/lon  " + nOSMPlace.osmPlaceId,
                    nOSMPlace.toString()
                )
                logDebug("ufe1", nOSMPlace.toString())
                _osmPlace4LonLat.postValue(nOSMPlace)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from API: $e")
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
    fun processPlaceDetailOnOSMId(osmPlaceId: Long) {
        var thisOSMPlaceDetail = OSMPlaceDetail(0L, osmPlaceId, Date(), Date(), 0L, 0.0)
        logDebug("ufe-up", "process placeDetail")
        CoroutineScope(Dispatchers.IO).launch {
            if (lastOSMPlaceDetail.osmPlaceId == thisOSMPlaceDetail.osmPlaceId) {
                logDebug("ufe-up", "place is the same")
                //place not changed
                lastOSMPlaceDetail.lastSeen = Date()
                val duration =
                    TTProcess.getDifferenceInMinutes(
                        lastOSMPlaceDetail.firstSeen,
                        thisOSMPlaceDetail.lastSeen
                    )
                lastOSMPlaceDetail.durationMinutes = duration
                logDebug("ufe-up", "place is the same " + duration.toString())
                addLogEntry(
                    ALogType.GEO,
                    "Same Place Detail, duration =" + duration + "m", lastOSMPlaceDetail.toString()
                )
                updatePlaceDetail(lastOSMPlaceDetail)

                _osmPlaceDetail.postValue(lastOSMPlaceDetail)
                getAllOSMPlacesDetailFromRoom()
                getAllOSMPlacesExtFromRoom()
            } else
                try {
                    logDebug("ufe", "OSMPlace detail new " + thisOSMPlaceDetail.toString())
                    val ID = database.TickTraxDao.insertOSMPlaceDetail(thisOSMPlaceDetail)
                    thisOSMPlaceDetail.osmPlaceDetailId = ID
                    _osmPlaceDetail.postValue(thisOSMPlaceDetail)

                    getAllOSMPlacesDetailFromRoom()
                    getAllOSMPlacesExtFromRoom()
                    logDebug("ufe", "OSMPlace detail new " + thisOSMPlaceDetail.toString())

                    addLogEntry(
                        ALogType.GEO,
                        "OSM Place Detail processed  " + ID + " for " + thisOSMPlaceDetail.osmPlaceId
                    )
                } catch (e: Exception) {
                    logError("ufe", e.toString())
                }
        }
        if (_osmPlaceDetail.value != null) {
            lastOSMPlaceDetail = _osmPlaceDetail.value!!
        }
        updateStats()
    }

    fun updatePlaceDetail(placeDetail: OSMPlaceDetail) {
        addLogEntry(
            ALogType.GEO,
            "try OSM Place Detail updated  "
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val no = database.TickTraxDao.updateOSMPlaceDetail(placeDetail)
                _osmPlaceDetail.postValue(placeDetail)

                getAllOSMPlacesDetailFromRoom()
                getAllOSMPlacesExtFromRoom()
                addLogEntry(
                    ALogType.GEO,
                    no.toString() + " Place Detail updated " + _osmPlace.value?.lastSeen.toString()
                )
            } catch (e: Exception) {
                logError("ufe", e.toString())
            }
            updateStats()
        }
    }

    fun getAllOSMPlacesDetailFromRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allOSMPlaceDetailS = database.TickTraxDao.getAllOSMPlaceDetails()
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _osmPlaceDetailS.postValue(allOSMPlaceDetailS)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM:" + e.toString())
            }
            updateStats()
        }
    }

    fun getAllOSMPlaceDetailFromRoom4Id(OSMPlaceID: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var OSMPlaceDetailS = database.TickTraxDao.getAllOSMPlaceDetails(OSMPlaceID)
                //   logDebug("ufe", "I read " + allOSMPlaces.size + " OSMPlaces from ROOM")
                _osmPlaceDetailS4Id.postValue(OSMPlaceDetailS)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM:" + e.toString())
            }
            updateStats()
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
                _osmPlaceExtS.postValue(allOSMPlaceExt)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
            updateStats()
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
            updateStats()
        }
    }

    fun processLocationOnLatLon(OSMPlaceId: Long, lat: Double, lon: Double, alt: Double) {
        val newLoc = TTLocation(
            createUniqueKey(lat, lon),
            lon,
            lat,
            alt,
            OSMPlaceId,
            Date(),
            Date(),
            0.0
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ID = database.TickTraxDao.insertLocation(newLoc)
                //    newLoc.LocationId = ID
                _location.postValue(newLoc)

                getAllLocationsFromRoom()
                getAllLocationsExtFromRoom()
                if (_location.value != null) {
                    lastLocation = _location.value!!
                }
                processLocationDetail(newLoc)
                addLogEntry(
                    ALogType.GEO,
                    "Location processed for " + newLoc.OSMPlaceId,
                    " for " + newLoc.OSMPlaceId + "\n" + newLoc.toString()
                )
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
        }
    }

    fun updateLocation(newLoc: TTLocation) {
        CoroutineScope(Dispatchers.IO).launch {
//            addLogEntry(
//                ALogType.GEO,
//                "try Location update " + newLoc.LocationId + " for " + newLoc.OSMPlaceId
//            )
            try {
                val no = database.TickTraxDao.insertLocation(newLoc)
                _location.postValue(newLoc)

                getAllLocationsFromRoom()
                getAllLocationsExtFromRoom()

                addLogEntry(
                    ALogType.GEO,
                    no.toString() + " Location update " + _location.value?.lastSeen
                )
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
            updateStats()
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
    fun processLocationDetail(location: TTLocation) {
        var locationDetail = TTLocationDetail(
            0L, location.LocationId, Date(), Date(), 0L, 0.0
        )
        if (lastLocationDetail.LocationId == locationDetail.LocationId) {
            //location not changed
            lastLocationDetail.lastSeen = Date()
            val duration =
                TTProcess.getDifferenceInMinutes(
                    lastLocationDetail.firstSeen,
                    locationDetail.lastSeen
                )
            lastLocationDetail.durationMinutes = duration
            addLogEntry(
                ALogType.GEO,
                "Same Location Detail, duration =" + duration + "m", lastLocationDetail.toString()
            )
            updateLocationDetail(lastLocationDetail)
            _locationDetail.postValue(lastLocationDetail)
            getAllLocationsDetailFromRoom()
            getAllLocationsExtFromRoom()
        } else
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val ID = database.TickTraxDao.insertLocationDetail(locationDetail)
                    locationDetail.LocationDetailId = ID
                    _locationDetail.postValue(locationDetail)

                    getAllLocationsDetailFromRoom()
                    getAllLocationsExtFromRoom()
                    if (_locationDetail.value != null) {
                        lastLocationDetail = _locationDetail.value!!
                    }
                    addLogEntry(
                        ALogType.GEO,
                        "Location Detail added " + locationDetail.LocationDetailId,
                        locationDetail.toString()
                    )
                } catch (e: Exception) {
                    logError("ufe", e.toString())
                }
            }
    }

    fun updateLocationDetail(locationDetail: TTLocationDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            addLogEntry(
                ALogType.GEO,
                "try Location Detail update "
            )
            try {
                val no = database.TickTraxDao.updateLocationDetail(locationDetail)
                _locationDetail.postValue(locationDetail)
                getAllLocationsDetailFromRoom()
                getAllLocationsExtFromRoom()
//                addLogEntry(
//                    ALogType.GEO,
//                    no.toString() + " Location Detail update " + _locationDetail.value?.lastSeen.toString()
//                )
            } catch (e: Exception) {
                logError("ufe", e.toString())
            }
            updateStats()
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
            updateStats()
        }
    }


    fun getAllLocationDetailFromRoom4Id(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var allLocationDetail = database.TickTraxDao.getAllLocationDetail(id)
                //   logDebug("ufe", "I read " + allLocations.size + " ttlocations from ROOM")
                _locationDetailS4Id.postValue(allLocationDetail)
            } catch (e: Exception) {
                logError("ufe", "Error loading Data from ROOM: $e")
            }
            updateStats()
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
            var allLocationExtS = database.TickTraxDao.getAllLocationExt()
            logDebug("ufe", "I read " + allLocationExtS.size + " locationExt from ROOM")
            _locationExtS.postValue(allLocationExtS)
        } catch (e: Exception) {
            logError("ufe", "Error loading Data from ROOM: $e")
        }
        updateStats()
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
            //logDebug("ufe", "loading Data from ROOM: ${_aLogDataS.value?.size}")
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

    /*---------------------------------------------------------------------------------------------
          _        _
      ___| |_ __ _| |_ ___
    / __| __/ _` | __/ __|
    \__ \ || (_| | |_\__ \
    |___/\__\__,_|\__|___/
      -----------------------------------------------------------------------------------------------
      */

    // --alogData----------------------
    private val _Stats = MutableLiveData<Stats>()
    val stats: LiveData<Stats>
        get() = _Stats

    fun updateStats() {
        var myStats = Stats(
            Date(),
            Date(),
            0L,
            0L,
            0L,
            0L,
            0L,
            0L
        )

        CoroutineScope(Dispatchers.IO).launch {
            var minFirst = _locationDetailS.value?.minByOrNull { it.firstSeen }
            var maxLast = _locationDetailS.value?.maxByOrNull { it.lastSeen }
            var noOfPalce= _osmPlaceS.value?.size
            var noOfPaceNot0= _osmPlaceExtS.value?.count{ it.durationMinutes != 0L}
            var durationPlaces = _osmPlaceExtS.value?.sumOf{ it.durationMinutes }
            var noOfLocation = _locationS.value?.size
            var noOfLocationNot0 = _locationExtS.value?.count{ it.durationMinutes != 0L }
            var durationLocations = _locationExtS.value?.sumOf{ it.durationMinutes  }

            if (minFirst != null) {
                myStats.minFirstSeen = minFirst.firstSeen
            }
            if (maxLast != null) {
                myStats.maxLastSeen = maxLast.lastSeen
            }
            if (noOfPalce != null) {
                myStats.noPlaces = noOfPalce.toLong()
            }
            if (noOfLocation != null) {
                myStats.noLocation = noOfLocation.toLong()
            }
            if (noOfPaceNot0 != null) {
                myStats.noPlacesNot0= noOfPaceNot0.toLong()
            }
            if (noOfLocationNot0 != null) {
                myStats.noLocationNot0 = noOfLocationNot0.toLong()
            }
            if (durationPlaces != null) {
                myStats.durationPlaces = durationPlaces
            }
            if (durationLocations != null) {
                myStats.durationLocation = durationLocations
            }

            _Stats.postValue(myStats)
        }
    }
}


