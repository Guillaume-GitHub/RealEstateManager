package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_new_estate.*
import java.util.zip.Inflater


class NewEstateActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_estate)

        this.configToolbar()
        this.displayNewEstateFragment()
    }

    // For toolbar controls
    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_new_estate_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    // Show fragment
    private fun displayNewEstateFragment(){
        val newEstateFragment = NewEstateFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_new_estate_frame_layout, newEstateFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.activity_new_estate_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
