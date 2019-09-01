package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.DraftDao
import com.openclassrooms.realestatemanager.model.entity.Draft

class DraftDataRepository(private val draftDao: DraftDao) {

    fun getDraft(dratUid: Long): LiveData<Draft>? {
        return this.draftDao.getDraft(dratUid)
    }

    fun getAllDraft(): LiveData<List<Draft>>? {
        return this.draftDao.getAllDraft()
    }

    fun updateDraft(draft: Draft): Int {
        return this.draftDao.updateDraft(draft)
    }

    fun insertDraft(draft: Draft): Long {
        return this.draftDao.insertDraft(draft)
    }

    fun deleteDraft(draft: Draft): Int {
        return this.draftDao.deleteDraft(draft)
    }

    fun deleteAllDraft(): Int {
        return this.draftDao.deleteAllDraft()
    }
}