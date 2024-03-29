package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.model.entity.Agent

@Dao
interface AgentDao {
    @Query("SELECT * FROM agent ORDER BY surname")
    fun getAgents(): LiveData<List<Agent>>?

    @Query("SELECT * FROM agent WHERE uid= :agentUid")
    fun getAgent(agentUid: Long): LiveData<Agent>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAgent(agent: Agent): Long

    @Update
    fun updateAgent(agent: Agent)

    @Delete
    fun deleteAgent(agent: Agent)

}