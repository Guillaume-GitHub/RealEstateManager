package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_estate.*

class NewEstateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_estate)
        this.configToolbar()
    }


    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_new_estate_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
    }
}
