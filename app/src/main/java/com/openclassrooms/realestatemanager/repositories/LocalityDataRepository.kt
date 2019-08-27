package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.LocalityDao
import com.openclassrooms.realestatemanager.model.entity.Locality
import io.reactivex.Observable

class LocalityDataRepository(private val localityDao: LocalityDao){

    fun getLocality(localityUid: Long): LiveData<Locality> {
        return this.localityDao.getLocality(localityUid)
    }

    fun getObservableLocality(localityUid: Long): Observable<Locality> {
        return this.localityDao.getObservableLocality(localityUid)
    }

    fun getLocalities(): LiveData<List<Locality>>?{
        return this.localityDao.getLocalities()
    }

    fun insertLocality(locality: Locality): Long {
        return this.localityDao.insertLocality(locality)
    }

    fun deleteLocality(localityUid: Long){
        return this.localityDao.deleteLocality(localityUid)
    }
}