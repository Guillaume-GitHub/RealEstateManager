package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository
import com.openclassrooms.realestatemanager.repositories.DraftDataRepository
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.LocalityDataRepository
import java.util.concurrent.Executor

class ViewModelFactory(private val estateDataSource: EstateDataRepository,
                       private val localityDataSource: LocalityDataRepository,
                       private val agentDataSource: AgentDataRepository,
                       private val draftDataSource: DraftDataRepository,
                       private val executor: Executor): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstateViewModel::class.java)) {
            return EstateViewModel(estateDataSource, localityDataSource, agentDataSource, draftDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}