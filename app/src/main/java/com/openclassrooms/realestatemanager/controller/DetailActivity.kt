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
    private var estateViewModel: EstateViewModel? = null

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
        //this.configToolbar()
        this.displayDraftFragment()
    }


    private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    fun getViewModel(): EstateViewModel? {
        return if (this.estateViewModel != null) this.estateViewModel else null
    }
/*
    //Configure toolbar and navigation
    private fun configToolbar() {
        // Set the toolbar as support action bar
        setSupportActionBar(activity_detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
*/
    private fun displayDraftFragment(){
        val bundle = Bundle()
        bundle.putLong("estate_id",this.estateId)

        val fragment = DetailFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.activity_detail_frame_layout, fragment).commit()
    }
}