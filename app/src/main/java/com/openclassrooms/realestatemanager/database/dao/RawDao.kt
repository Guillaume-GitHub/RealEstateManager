package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.entity.Estate

@Dao
interface RawDao {
    @RawQuery(observedEntities = arrayOf(Estate::class))
    fun getRawResult(query: SupportSQLiteQuery): LiveData<List<Estate>>?
}