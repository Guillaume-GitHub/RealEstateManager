package com.openclassrooms.realestatemanager.Injections

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository
import com.openclassrooms.realestatemanager.repositories.DraftDataRepository
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.LocalityDataRepository
import com.openclassrooms.realestatemanager.viewModel.ViewModelFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Injection {

    companion object{

        private fun provideEstateDataSource(context: Context):EstateDataRepository{
            val db = AppDatabase.getInstance(context)
            return EstateDataRepository(db!!.estateDao())
        }

       private fun provideLocalityDataSource(context: Context):LocalityDataRepository{
            val db = AppDatabase.getInstance(context)
            return LocalityDataRepository(db!!.localityDao())
        }

        private fun provideAgentDataSource(context: Context):AgentDataRepository{
            val db = AppDatabase.getInstance(context)
            return AgentDataRepository(db!!.agentDao())
        }

        private fun provideDraftDataSource(context: Context):DraftDataRepository{
            val db = AppDatabase.getInstance(context)
            return DraftDataRepository(db!!.draftDao())
        }

        private fun provideExecutor():Executor{
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory(context: Context): ViewModelFactory{
            return ViewModelFactory(provideEstateDataSource(context),
                    provideLocalityDataSource(context),
                    provideAgentDataSource(context),
                    provideDraftDataSource(context),
                    provideExecutor())
        }
    }
}