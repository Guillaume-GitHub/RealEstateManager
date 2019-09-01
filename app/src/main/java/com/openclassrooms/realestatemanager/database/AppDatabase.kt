package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.*
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.dao.*
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.model.entity.Locality
import java.util.concurrent.Executors


@Database(entities = arrayOf(Estate::class, Locality::class, Agent::class, Draft::class), version = 1)
@TypeConverters(DataTransformer::class)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun estateDao(): EstateDao
    abstract fun localityDao(): LocalityDao
    abstract fun agentDao(): AgentDao
    abstract fun draftDao(): DraftDao
    abstract fun rawDao(): RawDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DB_NAME = "RealEstateDatabase.db"
        // DATA
        private var PREPOPULATE_AGENT = Agent(0, "Real Estate", "MANAGER", null)

        // INSTANCE
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = this.prepopulateDatabase(context)
                    }
                }
            }
            return INSTANCE
        }

        // Prepolutate Database Before use
        private fun prepopulateDatabase(context: Context): AppDatabase{
            return  Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DB_NAME)
                    .addCallback(object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            Executors.newSingleThreadExecutor().execute {
                                getInstance(context)?.agentDao()?.insertAgent(PREPOPULATE_AGENT)
                            }
                        }
                    })
                    .build()
        }
    }
}
