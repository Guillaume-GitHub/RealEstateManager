package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.model.entity.Locality
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository
import com.openclassrooms.realestatemanager.repositories.DraftDataRepository
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.LocalityDataRepository
import io.reactivex.*
import java.util.concurrent.Executor

class EstateViewModel(private val estateDataSource: EstateDataRepository,
                      private val localityDataSource: LocalityDataRepository,
                      private val agentDataSource: AgentDataRepository,
                      private val draftDataSource: DraftDataRepository,
                      private val executor: Executor): ViewModel(){

    // DATA
    private var lastEstate:LiveData<Estate>? = null
    private var latestEstates: LiveData<List<Estate>>? = null
    private var localities: LiveData<List<Locality>>? = null
    private var allEstates: LiveData<List<Estate>>? = null

    //***********************************************************
    // FOR ESTATE
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

    fun getAllEstates():LiveData<List<Estate>>?{
        if (this.allEstates == null){
            this.allEstates = this.estateDataSource.getLatestEstates()
        }
        return this.allEstates
    }

    fun insertEstate(estate: Estate): Single<Long> {
       return Single.just(this.estateDataSource.insertEstate(estate))
    }

    fun updateEstate(estate: Estate): Single<Int> {
        return Single.fromCallable{ this.estateDataSource.updateEstate(estate) }
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

    //***********************************************************
    // FOR DRAFT
    //***********************************************************

    fun getDraft(dratUid: Long): LiveData<Draft>? {
        return this.draftDataSource.getDraft(dratUid)
    }

    fun getAllDraft(): LiveData<List<Draft>>? {
        return this.draftDataSource.getAllDraft()
    }

    fun updateDraft(draft: Draft): Single<Int> {
        return Single.fromCallable{this.draftDataSource.updateDraft(draft) }
    }

    fun insertDraft(draft: Draft): Single<Long> {
        return Single.fromCallable { this.draftDataSource.insertDraft(draft) }
    }

    fun deleteDraft(draft: Draft): Single<Int> {
        return Single.fromCallable { this.draftDataSource.deleteDraft(draft) }
    }

    fun deleteAllDraft(): Single<Int> {
        return Single.fromCallable { this.draftDataSource.deleteAllDraft() }
    }
}