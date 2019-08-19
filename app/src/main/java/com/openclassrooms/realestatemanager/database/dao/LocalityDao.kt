package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Locality
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface LocalityDao {
    @Query("SELECT * FROM locality ORDER BY cities")
    fun getLocalities(): LiveData<List<Locality>>

    @Query("SELECT * FROM locality WHERE localityUid= :localityUid")
    fun getLocality(localityUid: Long): LiveData<Locality>

    @Query("SELECT * FROM locality WHERE localityUid= :localityUid")
    fun getObservableLocality(localityUid: Long): Observable<Locality>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocality(locality: Locality): Long

    @Query("DELETE FROM locality WHERE localityUid = :localityUid")
    fun deleteLocality(localityUid: Long)
}