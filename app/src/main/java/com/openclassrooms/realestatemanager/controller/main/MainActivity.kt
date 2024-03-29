package com.openclassrooms.realestatemanager.controller.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.estate.NewEstateActivity
import com.openclassrooms.realestatemanager.controller.draft.DraftActivity
import com.openclassrooms.realestatemanager.controller.maps.MapsActivity
import com.openclassrooms.realestatemanager.controller.profile.ProfileActivity
import com.openclassrooms.realestatemanager.controller.simulation.SimulationActivity
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var estateViewModel: EstateViewModel? = null
    private lateinit var homeFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ON CREATE ", "MAIN ACTIVITY")

        this.configureViewModel()
        this.displayHomeFragment()
        this.configToolbar()
        this.configureDrawerLayout()
    }

     private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    fun getViewModel(): EstateViewModel? {
        return if (this.estateViewModel != null) this.estateViewModel else null
    }

    private fun displayHomeFragment(){
        if(supportFragmentManager.findFragmentByTag("homeFragment") != null){
            this.homeFragment = supportFragmentManager.findFragmentByTag("homeFragment") as HomeFragment
        }
        else {
            this.homeFragment = HomeFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.activity_main_framelayout_list,this.homeFragment,"homeFragment").commit()
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
           R.id.activity_main_drawer_home -> if (!this.homeFragment.isVisible) this.displayHomeFragment()
           R.id.activity_main_drawer_new -> startActivity(Intent(this, NewEstateActivity::class.java))
           R.id.activity_main_drawer_draft -> startActivity(Intent(this, DraftActivity::class.java))
           R.id.activity_main_drawer_map -> startActivity(Intent(this, MapsActivity::class.java))
           R.id.activity_main_drawer_simulation -> startActivity(Intent(this, SimulationActivity::class.java))
           R.id.activity_main_drawer_profile -> startActivity(Intent(this, ProfileActivity::class.java))
       }
        activity_main_drawer_layout.closeDrawers()
        return true
    }

}
