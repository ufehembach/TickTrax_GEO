package de.ticktrax.ticktrax_geo.ui

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.ticktrax.de.ticktrax.ticktrax_geo.RepositoryProvider
import de.ticktrax.ticktrax_geo.data.TickTraxAppRepository
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB.Companion.getDatabase
import de.ticktrax.ticktrax_geo.data.remote.OSMGsonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

enum class LOADStatus {
    NIX, LOADREQUESTED, LOADED, NIXLOADED
}

class TemplateViewModel (application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    //private val TemplateRep = TemplateAppRepository(TemplateJsonApi,TemplateTxtApi)
    private val OSMRep = TickTraxAppRepository(OSMGsonApi,database)
    val genericEnv = OSMRep.genericEnvelope
    private val locationRepository = RepositoryProvider.locationRepository
    private val _saving = MutableLiveData<LOADStatus>()
    val saving: LiveData<LOADStatus>
        get() = _saving

    init {
        Log.d("ufe", "init viewmodel")
        loadGenericEnv()
    }

    fun loadGenericEnv() {
        Log.d("ufe", "Pre json corroutine")

        viewModelScope.launch {
            _saving.value = LOADStatus.LOADREQUESTED
            try {
                Log.d("ufe", "pre getEnv  ")
                genericRep.getGenericEnv()
                Log.d("ufe", "post1 getEnv  ")
                _saving.value = LOADStatus.LOADED
                Log.d("ufe", "post2 getEnv  ")
            } catch (ex: Exception) {
                android.util.Log.e("ufe", "Error Loading " + ex)
                if (genericEnv.value!!.media.isNullOrEmpty()) {
                    _saving.value = LOADStatus.NIXLOADED
                } else {
                    _saving.value = LOADStatus.LOADED
                }
            }
        }
        Log.d("ufe", "post json corroutine")
    }

    fun reloadGenericEnv() {
        Log.d("ufe", "Pre reload corroutine")
        viewModelScope.launch(Dispatchers.IO) {
            genericRep.getGenericEnv()
        }
        Log.d("ufe", "post txt corroutine")
    }

    private val _geo = locationRepository.locationData
    //val geo: LiveData<Pair<Double,Double>>
    val geo: LiveData<Location>
        get() = _geo


}