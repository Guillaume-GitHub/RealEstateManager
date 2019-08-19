package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate WHERE saleDate IS NULL ORDER BY publishedDate DESC LIMIT 10")
    fun getLatestEstates():LiveData<List<Estate>>

    @Query("SELECT * FROM estate ORDER BY estateUid DESC LIMIT :nbOfEstates")
    fun getEstatesWithCursor(nbOfEstates: Long):Cursor

    @Query("SELECT * FROM estate WHERE estateUid = :estateUid")
    fun getEstate(estateUid: Long):LiveData<Estate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstate(estate: Estate) :Long

    @Query("DELETE FROM estate WHERE estateUid = :estateUid")
    fun deleteEstate(estateUid: Long)
}