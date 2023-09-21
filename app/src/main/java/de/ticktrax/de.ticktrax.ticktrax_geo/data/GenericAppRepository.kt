package de.ticktrax.ticktrax_geo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ticktrax.ticktrax_geo.data.datamodels.GenericData
import de.ticktrax.ticktrax_geo.data.datamodels.GenericEnvelope
import de.ticktrax.ticktrax_geo.data.local.TemplateDB
import de.ticktrax.ticktrax_geo.data.remote.TemplateJsonApi

// todo
/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von Live Data dem Rest
 * der App zur Verfügung
 */
class GenericAppRepository(
    private val TemplateJsonApi: TemplateJsonApi,
    private val database: TemplateDB
    // private val TemplateTxtApi: TemplateTxtApi
) {

    private var _genericEnvelope = MutableLiveData<GenericEnvelope>()
    val genericEnvelope: LiveData<GenericEnvelope>
        get() = _genericEnvelope

    private var _genericData = MutableLiveData<List<GenericData>>()
    val genericData: LiveData<List<GenericData>>
        get() = _genericData

    suspend fun getGenericEnv() {
        try {
            Log.d("ufe", "load Data from API")
            val genericEnvelope = TemplateJsonApi.apiJsonService.getGenericEnvelope()
            Log.d("ufe", "Data from API ")
      //      Log.d("ufe", "Data from API " + _genericEnvelope.value.media.size)
            _genericEnvelope.postValue(genericEnvelope)
           database.TemplateDao.insertList(genericEnvelope.media)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }
    suspend fun getGeneric(){
        try {
            Log.d("ufe", "load Data from API")
            val daoDB = database.TemplateDao.getAll()
            Log.d("ufe", "Data from API ")
            //      Log.d("ufe", "Data from API " + _genericEnvelope.value.media.size)
            _genericData.postValue(daoDB.value)
        } catch (e: Exception) {
            Log.e("ufe", "Error loading Data from API: $e")
        }
    }

    suspend fun reloadTemplateEnv() {

        //Load new Templateenv from API
        val newEnv = TemplateJsonApi.apiJsonService.getGenericEnvelope()
        //Post new data into LiveData
        _genericEnvelope.postValue(newEnv)
    }

}


