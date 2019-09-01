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

    companion object{
        const val FRAGMENT_TAG = "new_estate_frag"
    }

    private lateinit var newEstateFragment: NewEstateFragment
    private  var estateId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_estate)

        this.estateId = intent.getLongExtra("estate_id", -1)

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

        val frag  = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as NewEstateFragment?

        if (frag != null) this.newEstateFragment = frag
        else this.newEstateFragment = NewEstateFragment()

        if (this.estateId > -1){
            val bundle = Bundle()
            bundle.putLong("estate_id", this.estateId)
            newEstateFragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction().replace(R.id.activity_new_estate_frame_layout, newEstateFragment, FRAGMENT_TAG).commit()
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as NewEstateFragment?
        if (frag != null) frag.onBackPressed()
        else super.onBackPressed()
    }
}
