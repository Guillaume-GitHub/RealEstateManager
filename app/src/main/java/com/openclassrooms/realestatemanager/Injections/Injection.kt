package com.openclassrooms.realestatemanager.Injections

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.viewModel.ViewModelFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Injection {

    companion object{

        fun provideEstateDataSource(context: Context):EstateDataRepository{
            val db = AppDatabase.getInstance(context)
            return EstateDataRepository(db!!.estateDao())
        }

        fun provideExecutor():Executor{
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory(context: Context): ViewModelFactory{
            return ViewModelFactory(provideEstateDataSource(context), provideExecutor())
        }
    }
}