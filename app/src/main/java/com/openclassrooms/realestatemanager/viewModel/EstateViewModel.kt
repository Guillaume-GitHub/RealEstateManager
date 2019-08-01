package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import java.util.concurrent.Executor

class EstateViewModel(private val estateDataSource: EstateDataRepository, private val executor: Executor): ViewModel() {

    // DATA
    private var currentEstate:LiveData<Estate>? = null

    fun init(estateUid: Long) {
        if (this.currentEstate != null) {
            return
        }
        currentEstate = estateDataSource.getEstate(estateUid)
    }

    // FOR ESTATE
    fun getEstate(uid: Long): LiveData<Estate>?{
        return currentEstate
    }

    fun getLatestEstate():LiveData<List<Estate>>?{
        return  this.estateDataSource.getLatestEstates()
    }

    fun insertEstate(estate: Estate){
        executor.execute {
            this.estateDataSource.insertEstate(estate)
        }
    }

    fun deleteEstate(uid: Long){
        executor.execute {
            this.estateDataSource.deleteEstate(uid)
        }
    }
}