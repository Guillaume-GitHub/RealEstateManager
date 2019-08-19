package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate

class EstateDataRepository(private val estateDao:EstateDao) {

    fun getEstate(estateId: Long): LiveData<Estate>{
        return this.estateDao.getEstate(estateId)
    }

    fun getLatestEstates(): LiveData<List<Estate>>?{
        return this.estateDao.getLatestEstates()
    }

    fun insertEstate(estate: Estate): Long{
       return this.estateDao.insertEstate(estate)
    }

    fun deleteEstate(uid: Long){
        return this.estateDao.deleteEstate(uid)
    }
}