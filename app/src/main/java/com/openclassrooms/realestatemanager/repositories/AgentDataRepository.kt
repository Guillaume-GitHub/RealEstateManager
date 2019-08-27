package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.AgentDao
import com.openclassrooms.realestatemanager.model.entity.Agent

class AgentDataRepository(private val agentDao: AgentDao) {

    fun getAgent(agentUid: Long): LiveData<Agent>? {
        return this.agentDao.getAgent(agentUid)
    }

    fun getAgents(): LiveData<List<Agent>>?{
        return this.agentDao.getAgents()
    }

    fun insertAgent(agent: Agent): Long {
        return this.agentDao.insertAgent(agent)
    }
}