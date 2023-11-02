package de.ticktrax.ticktrax_geo.ui

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.data.TickTraxAppRepository
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB.Companion.getDatabase
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
import de.ticktrax.ticktrax_geo.myTools.logDebug
import kotlinx.coroutines.launch
import java.lang.Exception

enum class LOADStatus {
    NIX, LOADREQUESTED, LOADED, NIXLOADED
}

class TickTraxViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)

    //    private val locationRepository = RepositoryProvider.locationRepository
    //    private val ttApRep = TickTraxAppRepository(OSMGsonApi, database)
    //    private val ttApRep = TickTraxAppRepository()
    private val ttApRep = ttRepositoryProvider.TickTraxRepository
    // so würde es auch gehen, dann müssen die beiden variablen im repository aber auch public sein
//    private val ttApRep = ttRepositoryProvider.TickTraxRepository.apply { oSMGsonApi = OSMGsonApi,
//        database = database }

    init {
        logDebug("ufe", "init viewmodel")
        ttApRep.setParas(OSMGsonApi, database)
        ttApRep.addLogEntry(ALogType.INFO,"Repository Initalized")
    }

    // ------------------------------------------------------------------------
    // ladeinfo
    private val _saving = MutableLiveData<LOADStatus>()
    val saving: LiveData<LOADStatus>
        get() = _saving

    // ------------------------------------------------------------------------
    // One OSM Place
    private val _osmPlace = ttApRep.OSMPlace
    val osmPlace: LiveData<OSMPlace>
        get() = _osmPlace

    // all OSM Places
    private val _osmPlaceS = ttApRep.OSMPlaceS
    val osmPlaceS: LiveData<List<OSMPlace>>
        get() = _osmPlaceS

    // ------------------------------------------------------------------------
    // geo location
    private val _ttLocation = ttApRep.ttLocation
    val ttLocation: LiveData<TTLocation>
        get() = _ttLocation

    private val _ttLocationS = ttApRep.ttLocationS
    val ttLocationS: LiveData<List<TTLocation>>
        get() = _ttLocationS
    // ------------------------------------------------------------------------
    // geo locationext
    private val _ttLocationExt = ttApRep.ttLocationExt
    val ttLocationExt: LiveData<TTLocationExt>
        get() = _ttLocationExt

    private val _ttLocationExtS = ttApRep.ttLocationExtS
    val ttLocationExtS: LiveData<List<TTLocationExt>>
        get() = _ttLocationExtS

    // ------------------------------------------------------------------------
    // geo location details
    private val _ttLocationDetail = ttApRep.ttLocationDetail
    val ttLocationDetail: LiveData<TTLocationDetail>
        get() = _ttLocationDetail

    private val _ttLocationDetailS = ttApRep.ttLocationDetailS
    val ttLocationDetailS: LiveData<List<TTLocationDetail>>
        get() = _ttLocationDetailS

    // ------------------------------------------------------------------------
    private val _alogData = ttApRep.alogData
    val alogData: LiveData<ALog>
        get() = _alogData

    private val _alogDataS = ttApRep.alogDataS
    val alogDataS: LiveData<List<ALog>>
        get() = _alogDataS
    fun aLog(type: ALogType, logText: String?) {
        ttApRep.addLogEntry(type, logText, "rep " + logText)
    }
    fun aLog(type: ALogType, logText: String?,logDetail:String?) {
        ttApRep.addLogEntry(type, logText, "rep " + logText)
    }

}