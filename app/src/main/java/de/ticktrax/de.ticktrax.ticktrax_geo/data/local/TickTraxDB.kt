package de.ticktrax.ticktrax_geo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace

@Database(entities = [OSMPlace::class], version = 1)
//@TypeConverters(Converters::class)
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