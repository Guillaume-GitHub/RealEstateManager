package com.openclassrooms.realestatemanager.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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


    // Configure Drawer Layout
    private fun configureDrawerLayout() {
        // config open/close drawer
        val drawerLayout = activity_main_drawer_layout
        val toggle = ActionBarDrawerToggle(this, drawerLayout, activity_main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)

        //config click on drawer items
        val navigationView = activity_main_nav_view
        navigationView.setNavigationItemSelectedListener(this)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.activity_main_drawer_new -> startActivity(Intent(this, NewEstateActivity::class.java))
           R.id.activity_main_drawer_draft -> startActivity(Intent(this, DraftActivity::class.java))
           R.id.activity_main_drawer_map -> startActivity(Intent(this, MapsActivity::class.java))
       }
        activity_main_drawer_layout.closeDrawers()
        return true
    }

}
