package de.ticktrax.ticktrax_geo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.local.TickTraxDB
import de.ticktrax.ticktrax_geo.data.remote.OSMJsonApi

// todo
/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von Live Data dem Rest
 * der App zur Verfügung
 */
class TickTraxAppRepository(
    private val OSMJsonApi: OSMJsonApi,
    private val database: TickTraxDB
    // private val TemplateTxtApi: TemplateTxtApi
) {

    private var _OSMPlace = MutableLiveData<OSMPlace>()
    val OSMPlace: LiveData<OSMPlace>
        get() = _OSMPlace

    suspend fun getPlaceFromOSM() {
        try {
            Log.d("ufe", "load Data from API")
            val OSMPlace = OSMJsonApi.apiJsonService.getOSMPlace()
            Log.d("ufe", "Data from API ")
      //      Log.d("ufe", "Data from API " + _OSMPlace.value.media.size)
            _OSMPlace.postValue(OSMPlace)
           database.TemplateDao.insert(OSMPlace)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }
    suspend fun getGeneric(){
        try {
            Log.d("ufe", "load Data from API")
            val daoDB = database.TemplateDao.getAll()
            Log.d("ufe", "Data from API ")
            //      Log.d("ufe", "Data from API " + _OSMPlace.value.media.size)
            _OSMPlace.postValue(daoDB.value)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }

}


