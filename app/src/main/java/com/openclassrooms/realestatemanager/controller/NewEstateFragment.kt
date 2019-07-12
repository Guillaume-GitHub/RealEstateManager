package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_new_estate.*

class NewEstateFragment(val estate: Estate? = null) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.estate != null) this.bind(estate)
    }

    //TODO : MANAGE NULL VALUES + FILL ALL VIEWS
    //fill views
    private fun bind(estate: Estate){
        fragment_new_estate_input_text_title.setText((estate.title))
        fragment_new_estate_input_text_type.setText((estate.category))
        fragment_new_estate_input_text_price.setText((estate.price.toString()))
        fragment_new_estate_input_text_location.setText((estate.address))

    }
}
