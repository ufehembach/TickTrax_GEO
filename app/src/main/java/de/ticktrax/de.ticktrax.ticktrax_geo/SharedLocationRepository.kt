package de.ticktrax.de.ticktrax.ticktrax_geo

import android.location.Location
import androidx.lifecycle.MutableLiveData

object RepositoryProvider {
    val locationRepository by lazy { SharedLocationRepository() }
}
//Repository for Location Data
//Shared Between ViewModel and LocationService
class SharedLocationRepository {

    //private val _locationData = MutableLiveData<Pair<Double, Double>>()
    private val _locationData = MutableLiveData<Location>()
    //val locationData: MutableLiveData<Pair<Double, Double>>
    val locationData: MutableLiveData<Location>
        get() = _locationData

    //fun setLocation(latitude: Double, longitude: Double) {
    fun setLocation(myLocation:Location) {
        //_locationData.postValue(Pair(latitude, longitude))
        _locationData.postValue(myLocation)
    }
}