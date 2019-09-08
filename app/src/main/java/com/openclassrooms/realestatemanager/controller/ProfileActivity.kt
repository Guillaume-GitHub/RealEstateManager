package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_profil.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileFragment: ProfileFragment
    private lateinit var editProfileFragment: EditProfileFragment
    lateinit var viewModel: EstateViewModel
    private var currentAgent: Agent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        this.configureViewModel()
        this.configToolbar()
        this.getCurrentAgent()

        activity_profile_card_view_content.setOnClickListener {
            this.showEditProfile()
        }
    }

    //************************* CONFIG *************************

    // For toolbar controls
    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    // Get args passed to bundle
    private fun getBundleArgs(): Bundle {
        val args = Bundle()
        args.putLong(ProfileFragment.ARG_UID, this.currentAgent!!.uid)
        args.putString(ProfileFragment.ARG_FIRST_NAME, this.currentAgent!!.name)
        args.putString(ProfileFragment.ARG_LAST_NAME, this.currentAgent!!.surname)
        if (this.currentAgent!!.image != null) args.putString(ProfileFragment.ARG_IMAGE_URI, Utils.getRealPathFromURI(this,this.currentAgent!!.image))

        return args
    }

    //************************* UI / FRAGMENTS *************************

    private fun showProfile(){
        if ( supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) != null )
            this.profileFragment = supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) as ProfileFragment
        else
            this.profileFragment = ProfileFragment()

        this.profileFragment.arguments = this.getBundleArgs()

        creation_profile_card_view.visibility = View.GONE
        activity_profile_frame_layout.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction().replace(R.id.activity_profile_frame_layout, this.profileFragment, ProfileFragment.FRAGMENT_TAG).commit()
    }

    private fun showEditProfile(){
        if ( supportFragmentManager.findFragmentByTag(EditProfileFragment.FRAGMENT_TAG) != null )
            this.editProfileFragment = supportFragmentManager.findFragmentByTag(EditProfileFragment.FRAGMENT_TAG) as EditProfileFragment
        else
            this.editProfileFragment = EditProfileFragment()

        creation_profile_card_view.visibility = View.GONE
        activity_profile_frame_layout.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.activity_profile_frame_layout, this.editProfileFragment, EditProfileFragment.FRAGMENT_TAG).commit()
    }

    //************************* DATABASE *************************

    // Fetch Agents from database
    private fun getCurrentAgent(){
        this.viewModel.getAgents()?.observe(this, Observer { agents ->
            if (agents != null && agents.size > 1){
                this.currentAgent = agents[0]
                this.showProfile()
            }
            else {
                this.showEditProfile()
            }
        })
    }
}
