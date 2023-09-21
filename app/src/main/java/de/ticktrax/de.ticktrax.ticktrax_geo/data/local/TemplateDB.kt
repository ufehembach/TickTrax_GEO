package de.ticktrax.ticktrax_geo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.ticktrax.ticktrax_geo.data.datamodels.GenericData

@Database(entities = [GenericData::class], version = 1)
//@TypeConverters(Converters::class)
abstract class TemplateDB : RoomDatabase() {

    abstract val TemplateDao: TemplateDao

    companion object {
        private lateinit var dbInstance: TemplateDB

        fun getDatabase(context: Context): TemplateDB {
            synchronized(this) {
                // Initialisiere Datenbank
                if (!this::dbInstance.isInitialized) {
                    dbInstance = Room.databaseBuilder(
                        context.applicationContext,
                        TemplateDB::class.java,
                        "contact_database"
                    ).allowMainThreadQueries().build()
                }
                return dbInstance
            }
        }
    }
}