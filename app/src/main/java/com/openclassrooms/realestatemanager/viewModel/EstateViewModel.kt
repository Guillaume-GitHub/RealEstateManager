package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Locality
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.LocalityDataRepository
import io.reactivex.*
import java.util.concurrent.Executor

class EstateViewModel(private val estateDataSource: EstateDataRepository,
                      private val localityDataSource: LocalityDataRepository,
                      private val agentDataSource: AgentDataRepository,
                      private val executor: Executor): ViewModel(){

    // DATA
    private var lastEstate:LiveData<Estate>? = null
    private var latestEstates: LiveData<List<Estate>>? = null
    private var localities: LiveData<List<Locality>>? = null

    //***********************************************************
    // FOR LOCALITY
    //***********************************************************

    fun getEstate(uid: Long): LiveData<Estate>?{
        if (this.lastEstate == null) lastEstate = this.estateDataSource.getEstate(uid)
        return lastEstate
    }

    fun getLatestEstate():LiveData<List<Estate>>?{
        if (this.latestEstates == null){
            this.latestEstates = this.estateDataSource.getLatestEstates()
        }
        return this.latestEstates
    }

    fun insertEstate(estate: Estate): Single<Long>{
       return Single.just(this.estateDataSource.insertEstate(estate))
    }

    fun deleteEstate(uid: Long){
        executor.execute {
            this.estateDataSource.deleteEstate(uid)
        }
    }

    //***********************************************************
    // FOR LOCALITY
    //***********************************************************

    fun getLocalities(): LiveData<List<Locality>>?{
        if (localities == null) localities = this.localityDataSource.getLocalities()
        return localities
    }

    fun getLocality(localityUid: Long): LiveData<Locality>?{
      return this.localityDataSource.getLocality(localityUid)
    }

    fun getObservableLocality(localityUid: Long): Observable<Locality>{
        return this.localityDataSource.getObservableLocality(localityUid)
    }

    fun insertLocality(locality: Locality): Single<Long> {
        return Single.fromCallable{ this.localityDataSource.insertLocality(locality) }
    }

    //***********************************************************
    // FOR AGENT
    //***********************************************************

    fun getAgents(): LiveData<List<Agent>>?{
       return this.agentDataSource.getAgents()
    }

    fun getAgent(agentUid: Long): LiveData<Agent>?{
        return this.agentDataSource.getAgent(agentUid)
    }

    fun insertAgent(agent: Agent){
        executor.execute {
            this.agentDataSource.insertAgent(agent)
        }
    }
}