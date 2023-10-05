package de.ticktrax.ticktrax_geo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.LonLatAltRoom
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.LonLatAlt_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPLACES_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace


@Dao
interface TickTraxDao {
    // for export
 //   @get:Query("SELECT * FROM " + OSMPLACES_TBL_NAME)
 //   val allOSMPlaces: List<OSMPlace?>?

    // --------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOSMPlace(OSMPlace: OSMPlace)

    @Update
    fun updateOSMPlace(OSMPlace: OSMPlace)

    @Query("SELECT * FROM " + OSMPLACES_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllOSMPlaces(): List<OSMPlace>

    //   @Query("SELECT * FROM motiondata_table WHERE id = :id")
//    fun getById(id: Long): LiveData<List<MotionData>>

    //  @Query("DELETE FROM motion_table WHERE id = :id")
    //   fun delete(id: Long)

    @Query("DELETE FROM " + OSMPLACES_TBL_NAME)
    fun deleteAllOSMPlaces()

    @Query("SELECT COUNT(*) FROM " + OSMPLACES_TBL_NAME)
    fun countOSMPlaces(): Long

    // -------------------------------------------------
    // lon lat alt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLonLatAlt(lonLatAlt: LonLatAltRoom)

    @Query("SELECT * FROM " + LonLatAlt_TBL_NAME)
    fun getAllLonLatAlt(): List<LonLatAltRoom>


}