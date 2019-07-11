package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R

class DraftActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)
        this.displayDraftFragment()
    }

    private fun displayDraftFragment(){
        val draftFragment = DraftFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_draft_frame_layout,draftFragment).commit()
    }

}
