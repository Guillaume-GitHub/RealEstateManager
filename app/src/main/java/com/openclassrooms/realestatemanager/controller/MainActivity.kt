package com.openclassrooms.realestatemanager.controller

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
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var estateViewModel: EstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ON CREATE ", "MAIN ACTIVITY")

        this.configureViewModel()
        this.displayHomeFragment(HomeFragment(estateViewModel))
        this.configToolbar()
        this.configureDrawerLayout()
    }

    private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    private fun displayHomeFragment(fragment: HomeFragment){
        supportFragmentManager.beginTransaction().replace(R.id.activity_main_framelayout_list,fragment,"estateFragment").commit()
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

    override fun onStart() {
        Log.d("ON START ", "MAIN ACTIVITY")
        super.onStart()
    }

    override fun onPause() {
        Log.d("ON PAUSE ", "MAIN ACTIVITY")
        super.onPause()
    }

    override fun onStop() {
        Log.d("ON STOP ", "MAIN ACTIVITY")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("ON DESTROY ", "MAIN ACTIVITY")
        super.onDestroy()
    }

    override fun onResume() {
        Log.d("ON RESUME ", "MAIN ACTIVITY")
        super.onResume()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentByTag("detailFragment") != null) {
            Log.d("FRAG NON NULL", "??????")
            val frag : DetailFragment = supportFragmentManager.findFragmentByTag("detailFragment") as DetailFragment
            if (frag.isVisible){
                Log.d("FRAG VISISBLE", "??????")
                displayHomeFragment(HomeFragment(estateViewModel))
            }
        }
        else {
            super.onBackPressed()
        }
    }
}
