package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R

class ProfileActivity : AppCompatActivity() {

    companion object{
        private const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
    }

    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        this.displayFragment()
    }

    private fun displayFragment(){
        if ( supportFragmentManager.findFragmentByTag(PROFILE_FRAGMENT_TAG) != null )
            this.profileFragment = supportFragmentManager.findFragmentByTag(PROFILE_FRAGMENT_TAG) as ProfileFragment
        else
            this.profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction().replace(R.id.activity_profile_frame_layout, this.profileFragment, PROFILE_FRAGMENT_TAG).commit()
    }

}
