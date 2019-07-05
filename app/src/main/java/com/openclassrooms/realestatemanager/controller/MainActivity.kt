package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.displayListFragment()
    }

    private fun displayListFragment(){
        val itemFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_main_framelayout_list,itemFragment).commit()
    }
}
