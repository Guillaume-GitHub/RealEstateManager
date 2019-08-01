package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.*
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import androidx.room.Room
import com.openclassrooms.realestatemanager.model.Estate


@Database(entities = arrayOf(Estate::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun estateDao(): EstateDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DB_NAME = "RealEstateDatabase.db"

        // INSTANCE
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java, DB_NAME)
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
