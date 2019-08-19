package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private var estateId : Long = 0
    private lateinit var estateViewModel: EstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.estateId = intent?.extras?.getLong("ESTATE_ID")!!
        }
        catch (error: NullPointerException){
            this.finish()
        }

        setContentView(R.layout.activity_detail)
        this.configureViewModel()
        this.configToolbar()
        this.displayDraftFragment()
    }


    private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    //Configure toolbar and navigation
    private fun configToolbar() {
        // Set the toolbar as support action bar
        setSupportActionBar(activity_detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun displayDraftFragment(){
        supportFragmentManager.beginTransaction().add(R.id.activity_detail_frame_layout, DetailFragment(estateViewModel, estateId)).commit()
    }
}