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
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceExt
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail_TBL_NAME
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt


@Dao
interface TickTraxDao {
    // for export
    //   @get:Query("SELECT * FROM " + OSMPLACES_TBL_NAME)
    //   val allOSMPlaces: List<OSMPlace?>?


    // ----OSMPlace----------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOSMPlace(OSMPlace: OSMPlace)

    @Update
    fun updateOSMPlace(OSMPlace: OSMPlace)

    @Query("SELECT * FROM " + OSMPlace_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllOSMPlaces(): List<OSMPlace>

    @Query("DELETE FROM " + OSMPlace_TBL_NAME)
    fun deleteAllOSMPlaces()

    @Query("SELECT COUNT(*) FROM " + OSMPlace_TBL_NAME)
    fun countOSMPlaces(): Long

    @Query("SELECT * FROM " + OSMPlace_TBL_NAME + " WHERE OSMPlaceId = :OSMPlaceId")
    fun getOSMPlace4Id(OSMPlaceId: Long): OSMPlace

    // -------------------------------------------------
    // OSMPLaceDetails
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateOSMPlaceDetail(OSMPlace: OSMPlaceDetail)

    @Query("SELECT * FROM " + OSMPlaceDetail_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllOSMPlaceDetails(): List<OSMPlaceDetail>

    @Query("SELECT * FROM " + OSMPlaceDetail_TBL_NAME + " WHERE OSMPlaceId = :OSMPlaceId order BY OSMPlaceDetailId DESC LIMIT 1")
    fun getAOSMPlaceDetail(OSMPlaceId: Long): OSMPlaceDetail

    @Query("SELECT * FROM " + OSMPlaceDetail_TBL_NAME + " WHERE OSMPlaceId = :OSMPlaceId order BY OSMPlaceDetailId DESC")
    fun getAallOSMPlaceDetail(OSMPlaceId: Long): List<OSMPlaceDetail>

    @Query("SELECT * FROM " + OSMPlaceDetail_TBL_NAME + " WHERE OSMPlaceId = :OSMPlaceId  ORDER BY lastSeen DESC")
    fun getAOSMPlaceDetails(OSMPlaceId: Long): List<OSMPlaceDetail>

    @Query("SELECT * FROM " + OSMPlaceDetail_TBL_NAME + " ORDER BY OSMPlaceDetailId DESC LIMIT 1")
    fun getAOSMPlaceDetailLatestEntry(): OSMPlaceDetail?
    // -------------------------------------------------
    // OSMPLaceExt
    @Query("SELECT *, (SELECT SUM(durationMinutes) FROM " + OSMPlaceDetail_TBL_NAME + " WHERE " + OSMPlaceDetail_TBL_NAME + ".OSMPLaceId = " + OSMPlace_TBL_NAME + ".OSMPLaceId) as durationSum FROM " + OSMPlace_TBL_NAME + " order by lastSeen desc")
   /* @Query(
        "SELECT " +
                "$OSMPlace_TBL_NAME.*, " +
                "(SELECT SUM($OSMPlaceDetail_TBL_NAME.durationMinutes) " +
                "FROM $OSMPlaceDetail_TBL_NAME " +
                "WHERE $OSMPlaceDetail_TBL_NAME.OSMPLaceId = $OSMPlace_TBL_NAME.OSMPLaceId) as durationSum " +
                "FROM $OSMPlace_TBL_NAME " +
                "WHERE (SELECT SUM($OSMPlaceDetail_TBL_NAME.durationMinutes) " +
                "FROM $OSMPlaceDetail_TBL_NAME " +
                "WHERE $OSMPlaceDetail_TBL_NAME.OSMPLaceId = $OSMPlace_TBL_NAME.OSMPLaceId) > 0 " +
                "ORDER BY lastSeen DESC"
    )*/
    fun getAllOSMPlaceExt(): List<OSMPlaceExt>

    // -------------------------------------------------
    // Location
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLocation(Location: TTLocation)

    @Query("SELECT * FROM " + TTLocation_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllLocations(): List<TTLocation>

    @Query("SELECT * FROM " + TTLocation_TBL_NAME + " WHERE LocationId = :locationId")
    fun getALocations(locationId: Long): TTLocation

    @Query("SELECT * FROM " + TTLocation_TBL_NAME + " ORDER BY lastSeen DESC LIMIT 1")
    fun getAlocationLatestEntry(): TTLocation?

    // -------------------------------------------------
    // LocationDetails
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLocationDetail(Location: TTLocationDetail)

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " ORDER BY lastSeen DESC")
    fun getAllLocationDetails(): List<TTLocationDetail>

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId order BY LocationDetailId DESC LIMIT 1")
    fun getALocationDetail(LocationId: Long): TTLocationDetail

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId order BY lastseen DESC")
    fun getAllLocationDetail(LocationId: Long): List<TTLocationDetail>

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " WHERE LocationId = :LocationId")
    fun getALocationDetails(LocationId: Long): List<TTLocationDetail>

    @Query("SELECT * FROM " + TTLocationDetail_TBL_NAME + " ORDER BY LocationDetailId DESC LIMIT 1")
    fun getAlocationDetailLatestEntry(): TTLocationDetail?

    // -------------------------------------------------
    // LocationExt
    @Query("SELECT *, (SELECT SUM(durationMinutes) FROM " + TTLocationDetail_TBL_NAME + " WHERE " + TTLocationDetail_TBL_NAME + ".LocationId = " + TTLocation_TBL_NAME + ".LocationId ) as durationSum FROM " + TTLocation_TBL_NAME + " order by lastSeen Desc")
//    @Query(
//        "SELECT *, " +
//                "(SELECT SUM(durationMinutes) " +
//                "FROM $TTLocationDetail_TBL_NAME " +
//                "WHERE $TTLocationDetail_TBL_NAME.LocationId = $TTLocation_TBL_NAME.LocationId) as durationSum " +
//                "FROM $TTLocation_TBL_NAME " +
//                "WHERE (SELECT SUM(durationMinutes) " +
//                "FROM $TTLocationDetail_TBL_NAME " +
//                "WHERE $TTLocationDetail_TBL_NAME.LocationId = $TTLocation_TBL_NAME.LocationId) > 0 " +
//                "ORDER BY lastSeen DESC"
//    )

    fun getAllLocationExt(): List<TTLocationExt>

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