package de.ticktrax.ticktrax_geo.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPLACES_TBL_NAME

@Dao
interface TickTraxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(itemData: OSMPlace)

    //   @Insert(onConflict = OnConflictStrategy.REPLACE)
    //   fun insertList(itemList:List<GenericData>)

    @Update
    fun updateItem(itemData: OSMPlace)

    @Query("SELECT * FROM " + OSMPLACES_TBL_NAME)
    fun getAll(): LiveData<List<OSMPlace>>

    //   @Query("SELECT * FROM motiondata_table WHERE id = :id")
//    fun getById(id: Long): LiveData<List<MotionData>>

    //  @Query("DELETE FROM motion_table WHERE id = :id")
    //   fun delete(id: Long)

    @Query("DELETE FROM "+ OSMPLACES_TBL_NAME)
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM " + OSMPLACES_TBL_NAME)
    fun count(): Long
}