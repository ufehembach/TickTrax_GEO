package de.ticktrax.ticktrax_geo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.data.datamodels.TTAggregation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation

@Database(entities = [TTAggregation::class, OSMPlace::class, TTLocation::class, ALog::class], version = 1)
abstract class TickTraxDB : RoomDatabase() {

    abstract val TickTraxDao: TickTraxDao

    companion object {
        private lateinit var dbInstance: TickTraxDB

        fun getDatabase(context: Context): TickTraxDB {
            synchronized(this) {
                // Initialisiere Datenbank
                if (!this::dbInstance.isInitialized) {
                    dbInstance = Room.databaseBuilder(
                        context.applicationContext,
                        TickTraxDB::class.java,
                        "ticktrax_database"
                    ).allowMainThreadQueries().build()
                }
                return dbInstance
            }
        }
    }
}
