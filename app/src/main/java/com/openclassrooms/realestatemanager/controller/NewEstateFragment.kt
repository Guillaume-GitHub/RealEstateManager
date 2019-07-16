package com.openclassrooms.realestatemanager.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_new_estate.*

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class NewEstateFragment(val estate: Estate? = null) : Fragment(){


    companion object{
        private const val AUTOCOMPLETE_REQUEST_CODE = 10
    }
    // Type of fields return in Place Object
    private val fields : List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.estate != null) this.bind(estate)

        // Get focus on autocomplete location
        fragment_new_estate_input_autocomplete_location.setOnFocusChangeListener(View.OnFocusChangeListener {
                    v, hasFocus -> if(hasFocus) this.startPlacesAutocomplete()
        })

    }

    //TODO : MANAGE NULL VALUES + FILL ALL VIEWS
    //fill views
    private fun bind(estate: Estate) {
        fragment_new_estate_input_text_title.setText((estate.title))
        fragment_new_estate_input_text_type.setText((estate.category))
        fragment_new_estate_input_text_price.setText((estate.price.toString()))
       // fragment_new_estate_input_autocomplete_location.setText((estate.address))

    }

    // Start PlacesAutocomplete activity -> get result in onActivityResult()
    private fun startPlacesAutocomplete(){
        Log.d("AUTOCOMPLETE", "OK")
        // Init Places with api KEY
        Places.initialize(context!!, getString(R.string.google_maps_key))

        // Create a new Places client instance.
        val placesClient = Places.createClient(context!!)

        // Create autocomplete intent with display mode
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(context!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    // Get PlacesAutocomplete Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            AUTOCOMPLETE_REQUEST_CODE ->
                when(resultCode) {
                    AutocompleteActivity.RESULT_OK -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT OK")
                        if (Autocomplete.getPlaceFromIntent(data!!).address != null){
                            fragment_new_estate_input_autocomplete_location.setText(Autocomplete.getPlaceFromIntent(data).address)
                        }
                    }

                    AutocompleteActivity.RESULT_ERROR -> {
                        Log.i(this.javaClass.simpleName, Autocomplete.getStatusFromIntent(data!!).statusMessage)
                    }

                    AutocompleteActivity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT CANCELED")
                    }
                }
        }
    }
}

