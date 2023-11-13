package de.ticktrax.ticktrax_geo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
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
    val osmPlace = ttApRep.OSMPlace
    val osmPlaceS = ttApRep.OSMPlaceS
    val osmPlaceExt = ttApRep.OSMPlaceExt
    val osmPlaceExtS = ttApRep.OSMPlaceExtS
    // ------------------------------------------------------------------------
    val osmPlace4Id = ttApRep._OSMPlace4ID
    val osmPlaceDetailS4Id = ttApRep._OSMPlaceDetailS4Id
   /* fun OSMPLacesDetailS4IdSetId(OSMId: Long) {
        ttApRep.getAllOSMPlaceDetailFromRoom4Id(OSMId)
    }*/
    // ------------------------------------------------------------------------
    val osmPlace4LonLat = ttApRep._OSMPlace4LonLat
    // ------------------------------------------------------------------------
  /*  fun osmPlace4LonLatSetLonLat(lon: Double, lat: Double) {
        ttApRep.readOSMPlace4LonLat(lon, lat)
    }
*/
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
    fun locationDetailS4IdSetId(Id: Long) {
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