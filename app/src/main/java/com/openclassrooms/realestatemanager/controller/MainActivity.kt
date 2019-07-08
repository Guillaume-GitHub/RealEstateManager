package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.displayListFragment()
        this.configToolbar()
        this.configureDrawerLayout()
    }

    private fun displayListFragment(){
        val itemFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_main_framelayout_list,itemFragment).commit()
    }

    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_main_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_twotone_menu_white_24px)
    }


    // 2 - Configure Drawer Layout
    private fun configureDrawerLayout() {
        var drawerLayout = activity_main_drawer_layout
        var toggle = ActionBarDrawerToggle(this, drawerLayout, activity_main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
    }
}
