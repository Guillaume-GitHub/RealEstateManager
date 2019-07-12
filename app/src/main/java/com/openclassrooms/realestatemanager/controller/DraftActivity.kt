package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_draft.*

class DraftActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        this.configToolbar()
        this.displayDraftFragment()
    }

    private fun displayDraftFragment(){
        val draftFragment = DraftFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_draft_frame_layout,draftFragment).commit()
    }

    //Configure toolbar and navigation
    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_draft_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f

        //for navigation
        activity_draft_toolbar.setNavigationOnClickListener(View.OnClickListener {
            v: View? -> onBackPressed()  })
    }
}
