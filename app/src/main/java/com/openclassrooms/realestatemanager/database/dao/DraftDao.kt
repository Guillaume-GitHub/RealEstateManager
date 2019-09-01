package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.model.entity.Draft

@Dao
interface DraftDao {

    @Query("SELECT * FROM draft ORDER BY lastModification")
    fun getAllDraft(): LiveData<List<Draft>>?

    @Query("SELECT * FROM draft WHERE draftUid= :draftUid")
    fun getDraft(draftUid: Long): LiveData<Draft>?

    @Update
    fun updateDraft(draft: Draft): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDraft(draft: Draft): Long

    @Query("DELETE FROM draft")
    fun deleteAllDraft(): Int

    @Delete
    fun deleteDraft(draft: Draft): Int
}