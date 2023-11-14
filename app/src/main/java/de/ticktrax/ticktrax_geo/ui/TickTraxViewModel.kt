package de.ticktrax.ticktrax_geo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB.Companion.getDatabase
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
import de.ticktrax.ticktrax_geo.myTools.logDebug

enum class LOADStatus {
    NIX, LOADREQUESTED, LOADED, NIXLOADED
}

class TickTraxViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)

    //    private val locationRepository = RepositoryProvider.locationRepository
    //    private val ttApRep = TickTraxAppRepository(OSMGsonApi, database)
    //    private val ttApRep = TickTraxAppRepository()
    private val ttApRep = ttRepositoryProvider.TickTraxRepository
    //    so würde es auch gehen, dann müssen die beiden variablen im repository aber auch public sein
    //    private val ttApRep = ttRepositoryProvider.TickTraxRepository.apply { oSMGsonApi = OSMGsonApi,
    //    database = database }

    init {
        logDebug("ufe", "init viewmodel")
        ttApRep.setParas(OSMGsonApi, database)
        ttApRep.addLogEntry(ALogType.INFO, "ViewModel/Repository init")
        //    ttApRep.readInit()
    }

    // ------------------------------------------------------------------------

    // all OSM Places
    val osmPlace = ttApRep.osmPlace
    val osmPlaceS = ttApRep.osmPlaceS
    val osmPlaceExt = ttApRep.osmPlaceExt
    val osmPlaceExtS = ttApRep.osmPlaceExtS

    // ------------------------------------------------------------------------
    val osmPlace4Id = ttApRep._osmPlace4ID
    val osmPlaceDetailS4Id = ttApRep._osmPlaceDetailS4Id
    fun OSMPLacesDetailS4IdSetId(OSMId: Long) {
        ttApRep.getAllOSMPlaceDetailFromRoom4Id(OSMId)
    }
    // ------------------------------------------------------------------------
    val osmPlace4LonLat = ttApRep._osmPlace4LonLat
    // ------------------------------------------------------------------------
    fun osmPlace4LonLatSetLonLat(lon: Double, lat: Double) {
    ttApRep.readOSMPlace4LonLat(lon, lat)
    }

    // ------------------------------------------------------------------------
    // geo location
    val location = ttApRep.location
    val locationS = ttApRep.locationS

    // ------------------------------------------------------------------------
    // geo locationext
    val locationExt = ttApRep.locationExt
    val locationExtS = ttApRep.locationExtS

    // ------------------------------------------------------------------------
    // geo location details
    val locationDetail = ttApRep.locationDetail
    val locationDetailS = ttApRep.locationDetailS
    val locationDetailS4Id = ttApRep._locationDetailS4Id
    fun locationDetailS4IdSetId(Id: String) {
        ttApRep.getAllLocationDetailFromRoom4Id(Id)
    }

    // ------------------------------------------------------------------------
    val alogData = ttApRep.aLogData
    val alogDataS = ttApRep.aLogDataS

    fun aLog(type: ALogType, logText: String?) {
        ttApRep.addLogEntry(type, logText, "rep " + logText)
    }

    fun aLog(type: ALogType, logText: String?, logDetail: String?) {
        ttApRep.addLogEntry(type, logText, "rep " + logText)
    }
}