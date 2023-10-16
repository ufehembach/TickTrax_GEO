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
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB.Companion.getDatabase
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import de.ticktrax.ticktrax_geo.data.ttRepositoryProvider
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
        Log.d("ufe", "init viewmodel")
        ttApRep.setParas(OSMGsonApi, database)
        ttApRep.addLogEntry(ALogType.INFO,"Repository Initalized")
        //  loadAllOSMPlaces()
    }

    // ladeinfo
    private val _saving = MutableLiveData<LOADStatus>()
    val saving: LiveData<LOADStatus>
        get() = _saving

    // geo daten  lon lat
    private val _geo = ttApRep.locationData
    val geo: LiveData<Location>
        get() = _geo

    // One OSM Place
    // private val _osmPlace = MutableLiveData<OSMPlace>()
    private val _osmPlace = ttApRep.OSMPlace
    val osmPlace: LiveData<OSMPlace>
        get() = _osmPlace

    // all OSM Places
    //    private val _osmPlaces = MutableLiveData<List<OSMPlace>>()
    private val _osmPlaces = ttApRep.OSMPlaces
    val osmPlaces: LiveData<List<OSMPlace>>
        get() = _osmPlaces

    //    fun loadPlaceFromOSM() {
//        Log.d("ufe", "Pre json corroutine")
//        viewModelScope.launch {
//            _saving.value = LOADStatus.LOADREQUESTED
//            try {
//                Log.d("ufe", "pre getEnv  ")
//                ttApRep.getPlaceFromOSM(_geo.value!!.longitude, _geo.value!!.latitude)
//                Log.d("ufe", "post1 getEnv  ")
//                _saving.value = LOADStatus.LOADED
//                Log.d("ufe", "post2 getEnv  ")
//            } catch (ex: Exception) {
//                android.util.Log.e("ufe", "Error Loading " + ex)
//                if (ttApRep.OSMPlace.value == null) {
//                    _saving.value = LOADStatus.NIXLOADED
//                } else {
//                    _saving.value = LOADStatus.LOADED
//                }
//                ttApRep.OSMPlace.value
//            }
//        }
//        Log.d("ufe", "post json corroutine")
//    }
//
//    fun loadAllOSMPlaces() {
//        Log.d("ufe", "Pre load corroutine")
//        viewModelScope.launch {
//            _saving.value = LOADStatus.LOADREQUESTED
//            try {
//                Log.d("ufe", "pre getEnv  ")
//                ttApRep.getAllOSMPlacesFromRoom()
//                Log.d("ufe", "post1 getEnv  ")
//                _saving.value = LOADStatus.LOADED
//                Log.d("ufe", "post2 getEnv  ")
//            } catch (ex: Exception) {
//                android.util.Log.e("ufe", "Error Loading " + ex)
//                if (ttApRep.OSMPlaces.value == null) {
//                    _saving.value = LOADStatus.NIXLOADED
//                } else {
//                    _saving.value = LOADStatus.LOADED
//                }
//                ttApRep.OSMPlaces.value
//            }
//        }
//        Log.d("ufe", "post load corroutine")
//    }
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