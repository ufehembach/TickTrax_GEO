package de.ticktrax.ticktrax_geo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.ALog_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPLACES_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.TTAggregation
import de.ticktrax.ticktrax_geo.data.datamodels.TTAggregation_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt


@Dao
interface TickTraxDao {
    // for export
    //   @get:Query("SELECT * FROM " + OSMPLACES_TBL_NAME)
    //   val allOSMPlaces: List<OSMPlace?>?

    // -----TTAggregation---------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTTAggregation(TTAggregation: TTAggregation)

    @Update
    fun updateTTAggregation(TTAggregation: TTAggregation)

    @Query("SELECT * FROM " + TTAggregation_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllTTAggregations(): List<TTAggregation>


    @Query("DELETE FROM " + TTAggregation_TBL_NAME)
    fun deleteAllTTAggregations()

    @Query("SELECT COUNT(*) FROM " + TTAggregation_TBL_NAME)
    fun countTTAggregations(): Long

    // ----OSMPlace----------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOSMPlace(OSMPlace: OSMPlace)

    @Update
    fun updateOSMPlace(OSMPlace: OSMPlace)

    @Query("SELECT * FROM " + OSMPLACES_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllOSMPlaces(): List<OSMPlace>

    @Query("DELETE FROM " + OSMPLACES_TBL_NAME)
    fun deleteAllOSMPlaces()

    @Query("SELECT COUNT(*) FROM " + OSMPLACES_TBL_NAME)
    fun countOSMPlaces(): Long

    // -------------------------------------------------
    // Location
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLocation(Location: TTLocation)

    @Query("SELECT * FROM " + TTLocation_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllLocations(): List<TTLocation>

    @Query("SELECT * FROM " + TTLocation_TBL_NAME + " WHERE LocationId = :LocationId")
    fun getALocations(LocationId: Long): TTLocation
    @Query("SELECT * FROM "+ TTLocation_TBL_NAME  +  " ORDER BY lastSeen DESC LIMIT 1")
    fun getAlocationLatestEntry(): TTLocation?

    // -------------------------------------------------
    // LocationDetails
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLocationDetail(Location: TTLocationDetail)

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllLocationDetails(): List<TTLocationDetail>

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId order BY LocationDetailId DESC LIMIT 1")
    fun getALocationDetail(LocationId: Long): TTLocationDetail

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId")
    fun getALocationDetails(LocationId: Long): List<TTLocationDetail>

    @Query("SELECT * FROM "+ TTLocationDetail_TBL_NAME  +  " ORDER BY LocationDetailId DESC LIMIT 1")
    fun getAlocationDetailLatestEntry(): TTLocationDetail?

    // -------------------------------------------------
    // LocationExt
    @Query("SELECT *, (SELECT SUM(durationMinutes) FROM "+TTLocationDetail_TBL_NAME+ " WHERE "+ TTLocationDetail_TBL_NAME+".LocationId = " + TTLocation_TBL_NAME + ".LocationId) as durationSum FROM "+ TTLocation_TBL_NAME +" order by lastSeen")
    fun getAllLocationExt(): List<TTLocationExt>

    //@Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId order BY LocationDetailId DESC LIMIT 1")
    //fun getALocationExt(LocationId: Long): TTLocationExt

    // -------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogEntry(entry: ALog)

    @Query("SELECT * FROM " + ALog_TBL_NAME + " ORDER BY dateTime DESC")
    fun getAllLogEntries(): List<ALog>

    @Query("SELECT * FROM " + ALog_TBL_NAME + " ORDER BY aLogId ASC LIMIT 1")
    fun getSmallestALogIdEntry(): ALog?

    @Delete
    fun deleteALogId(entity: ALog)

    @Query("SELECT COUNT(*) FROM " + ALog_TBL_NAME)
    fun countLogEntries(): Int
}