package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.model.entity.Estate

class EstateDataRepository(private val estateDao:EstateDao) {

    fun getEstate(estateId: Long): LiveData<Estate>{
        return this.estateDao.getEstate(estateId)
    }

    fun getLatestEstates(): LiveData<List<Estate>>?{
        return this.estateDao.getLatestEstates()
    }

    fun getAllEstates(): LiveData<List<Estate>>?{
        return this.estateDao.getAllEstates()
    }

    fun getEstatesPostedBy(agentId: Long): LiveData<List<Estate>>?{
        return  this.estateDao.getEstatesPostedBy(agentId)
    }

    fun updateEstate(estate: Estate): Int{
        return this.estateDao.updateEstate(estate)
    }

    fun insertEstate(estate: Estate): Long{
       return this.estateDao.insertEstate(estate)
    }

    fun deleteEstate(uid: Long){
        return this.estateDao.deleteEstate(uid)
    }
}