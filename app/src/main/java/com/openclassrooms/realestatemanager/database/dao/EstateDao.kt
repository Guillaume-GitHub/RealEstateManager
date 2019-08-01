package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate ORDER BY uid DESC LIMIT 10")
    fun getLatestEstates():LiveData<List<Estate>>

   // @Query("SELECT * FROM estate WHERE LIMIT 10")
   // fun getLatestEstates():LiveData<List<Estate>>

    @Query("SELECT * FROM estate WHERE uid = :estateUid")
    fun getEstate(estateUid: Long):LiveData<Estate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstate(estate: Estate)

    @Query("DELETE FROM estate WHERE uid = :estateUid")
    fun deleteEstate(estateUid: Long)
}