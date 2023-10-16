package de.ticktrax.ticktrax_geo.data.local

import com.google.gson.Gson
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.local.TickTraxDao


class TickTraxExport(myDao: TickTraxDao) {
    private val myDao: TickTraxDao

    init {
        this.myDao = myDao
    }

    fun exportOSMPlacesToJson(): String {
        //val data: List<OSMPlace?>? = myDao.allOSMPlaces
        val data: List<OSMPlace?>? = myDao.getAllOSMPlaces()
        val gson = Gson()
        return gson.toJson(data)
    }
}
