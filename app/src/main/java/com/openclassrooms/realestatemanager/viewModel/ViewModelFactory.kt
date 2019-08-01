package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import java.util.concurrent.Executor

class ViewModelFactory(val estateDataSource: EstateDataRepository, val executor: Executor): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstateViewModel::class.java)) {
            return EstateViewModel(estateDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}