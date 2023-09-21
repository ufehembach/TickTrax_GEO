package de.ticktrax.ticktrax_geo.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.ticktrax.ticktrax_geo.data.datamodels.GenericData

@Dao
interface TemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(itemData: GenericData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(itemList:List<GenericData>)

    @Update
    fun updateItem(itemData: GenericData)

    @Query("SELECT * FROM genericdata_table")
    fun getAll(): LiveData<List<GenericData>>

 //   @Query("SELECT * FROM motiondata_table WHERE id = :id")
//    fun getById(id: Long): LiveData<List<MotionData>>

  //  @Query("DELETE FROM motion_table WHERE id = :id")
 //   fun delete(id: Long)

    @Query("DELETE FROM genericdata_table")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM genericdata_table")
    fun count(): Long
}