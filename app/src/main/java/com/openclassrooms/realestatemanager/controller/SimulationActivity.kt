package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_simulation.*

class SimulationActivity : AppCompatActivity() {

    private lateinit var simulationFragment: SimulationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulation)

        this.configToolbar()
        this.displaySimulationFragment()
    }

    private fun displaySimulationFragment(){
        if(supportFragmentManager.findFragmentByTag("simulationFragment") != null){
            this.simulationFragment = supportFragmentManager.findFragmentByTag("simulationFragment") as SimulationFragment
        }
        else {
            this.simulationFragment = SimulationFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.activity_simulation_frame_layout,this.simulationFragment,"simulationFragment").commit()
    }

    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_simulation_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_twotone_arrow_back_white_24px)
    }
}
