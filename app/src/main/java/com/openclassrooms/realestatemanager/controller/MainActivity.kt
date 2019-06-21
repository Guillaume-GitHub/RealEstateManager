package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.showFragment()
    }


    private fun showFragment(){
        val itemFragment = ItemListFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_main_framelayout,itemFragment).commit()
    }
}
