package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.model.entity.Estate

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate ORDER BY publishedDate DESC LIMIT 20")
    fun getLatestEstates():LiveData<List<Estate>>

    @Query("SELECT * FROM estate ORDER BY estateUid DESC LIMIT :nbOfEstates")
    fun getEstatesWithCursor(nbOfEstates: Long):Cursor

    @Query("SELECT * FROM estate WHERE estateUid = :estateUid")
    fun getEstate(estateUid: Long):LiveData<Estate>

    @Query("SELECT * FROM estate")
    fun getAllEstates():LiveData<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstate(estate: Estate) :Long

    @Query("DELETE FROM estate WHERE estateUid = :estateUid")
    fun deleteEstate(estateUid: Long)

    @Update(entity = Estate::class)
    fun updateEstate(estate: Estate): Int
}