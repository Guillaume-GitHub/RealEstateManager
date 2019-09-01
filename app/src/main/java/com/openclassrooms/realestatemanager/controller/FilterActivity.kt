package com.openclassrooms.realestatemanager.controller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.appyvet.materialrangebar.RangeBar
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.AddressCompenentsHelper
import com.openclassrooms.realestatemanager.Utils.FiltersHelper
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.nearby_chip_filters.*

class FilterActivity : AppCompatActivity(), RangeBar.OnRangeBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private var checkedBoxes = ArrayList<String>()
    private var checkedChips = ArrayList<String>()
    private var locationFilters = ArrayList<String>()
    // Type of fields return in Place Object
    private val fields: List<Place.Field> = listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.TYPES, Place.Field.ADDRESS)

    companion object {
        private const val MAX_PRICE = 1000000
        private const val MIN_PRICE = 0
        private const val INTERVAL_PRICE = 25000
        private const val MAX_SURFACE = 1000
        private const val MIN_SURFACE = 0
        private const val INTERVAL_SURFACE = 50
        private const val MAX_ROOM = 20
        private const val MIN_ROOM = 0
        private const val INTERVAL_ROOM = 1

        private const val AUTOCOMPLETE_RQ = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        this.configToolbar()

        this.setUpRangeBar(activity_filter_price_rangebar, MIN_PRICE, MAX_PRICE, INTERVAL_PRICE, this)
        this.setUpRangeBar(activity_filter_surface_rangebar, MIN_SURFACE, MAX_SURFACE, INTERVAL_SURFACE,this)
        this.setUpRangeBar(activity_filter_room_rangebar, MIN_ROOM, MAX_ROOM, INTERVAL_ROOM,this)
        this.setUpCheckBoxesListener()
        this.setUpAddLocation()
    }

    // Set up toolbar
    private fun configToolbar() {
        // Set the toolbar as support action bar
        setSupportActionBar(activity_filter_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    // Set up rangebar
    private fun setUpRangeBar(rangeBar: RangeBar,minVal: Int, maxVal: Int, interval: Int, listener: RangeBar.OnRangeBarChangeListener){
        rangeBar.tickStart = minVal.toFloat()
        rangeBar.tickEnd = maxVal.toFloat()
        rangeBar.setTickInterval(interval.toFloat())
        rangeBar.setOnRangeBarChangeListener(listener)

        when(rangeBar){
            activity_filter_price_rangebar -> {
                activity_filter_price_min_text.text = minVal.toString()
                activity_filter_price_max_text.text = maxVal.toString() + " +"
            }

            activity_filter_surface_rangebar -> {
                activity_filter_surface_min_text.text = minVal.toString()
                activity_filter_surface_max_text.text = maxVal.toString() + " +"
            }

            activity_filter_room_rangebar -> {
                activity_filter_room_min_text.text = minVal.toString()
                activity_filter_room_max_text.text = maxVal.toString() + " +"
            }
        }
    }

    private fun setUpCheckBoxesListener() {
        activity_filter_checkbox_house.setOnCheckedChangeListener(this)
        activity_filter_checkbox_house.isChecked = true
        activity_filter_checkbox_duplex.setOnCheckedChangeListener(this)
        activity_filter_checkbox_duplex.isChecked = true
        activity_filter_checkbox_flat.setOnCheckedChangeListener(this)
        activity_filter_checkbox_flat.isChecked = true
        activity_filter_checkbox_mansion.setOnCheckedChangeListener(this)
        activity_filter_checkbox_mansion.isChecked = true
        activity_filter_checkbox_penthouse.setOnCheckedChangeListener(this)
        activity_filter_checkbox_penthouse.isChecked = true
    }

    private fun setUpAddLocation(){
        activity_filter_location_editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) this.startPlacesAutocomplete()
        }
    }

    // Start PlacesAutocomplete activity -> get result in onActivityResult()
    private fun startPlacesAutocomplete() {
        // Init Places with api KEY
        Places.initialize(this, getString(R.string.google_maps_key))

        // Create a new Places client instance.
        val placesClient = Places.createClient(this)

        // Create autocomplete intent with display mode
        val typeFiler = TypeFilter.REGIONS
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_RQ)
    }

    // Range bar change listener
    override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
        if(rangeBar != null){
            when(rangeBar){
                activity_filter_price_rangebar -> {
                    activity_filter_price_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if((rightPinIndex * rangeBar.tickInterval).toInt() >= MAX_PRICE){
                        activity_filter_price_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    }
                    else{
                        activity_filter_price_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }

                activity_filter_surface_rangebar -> {
                    activity_filter_surface_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if ((rightPinIndex * rangeBar.tickInterval).toInt() >= MAX_SURFACE) {
                        activity_filter_surface_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    } else {
                        activity_filter_surface_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }

                activity_filter_room_rangebar -> {
                    activity_filter_room_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if ((rightPinIndex * rangeBar.tickInterval).toInt() >= MAX_ROOM) {
                        activity_filter_room_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    } else {
                        activity_filter_room_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }
            }
        }
    }

    override fun onTouchStarted(rangeBar: RangeBar?) {}

    override fun onTouchEnded(rangeBar: RangeBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id){
            // CheckBoxes checked state
            activity_filter_checkbox_house.id -> if(isChecked) this.checkedBoxes.add(getString(R.string.house)) else this.checkedBoxes.remove(getString(R.string.house))
            activity_filter_checkbox_duplex.id -> if(isChecked) this.checkedBoxes.add(getString(R.string.duplex)) else this.checkedBoxes.remove(getString(R.string.duplex))
            activity_filter_checkbox_flat.id -> if(isChecked) this.checkedBoxes.add(getString(R.string.flat)) else this.checkedBoxes.remove(getString(R.string.flat))
            activity_filter_checkbox_mansion.id -> if(isChecked) this.checkedBoxes.add(getString(R.string.mansion)) else this.checkedBoxes.remove(getString(R.string.mansion))
            activity_filter_checkbox_penthouse.id -> if(isChecked) this.checkedBoxes.add(getString(R.string.penthouse)) else this.checkedBoxes.remove(getString(R.string.penthouse))
        }

        Log.d("FILTERS = ", checkedBoxes.toString())
    }

    private fun getFilters(): ArrayList<String> {
        val filterList = ArrayList<String>()
        if (nearby_chip_hospital.isChecked) filterList.add(FiltersHelper.HEALTH_TAG)
        if (nearby_chip_restaurant.isChecked) filterList.add(FiltersHelper.RESTAURANT_TAG)
        if (nearby_chip_school.isChecked) filterList.add(FiltersHelper.SCHOOL_TAG)
        if (nearby_chip_store.isChecked) filterList.add(FiltersHelper.STORE_TAG)
        if (nearby_chip_supermarket.isChecked) filterList.add(FiltersHelper.SUPERMARKET_TAG)
        if (nearby_chip_transport.isChecked) filterList.add(FiltersHelper.TRANSPORT_TAG)
        if (nearby_chip_sport.isChecked) filterList.add(FiltersHelper.SPORT_TAG)

        return filterList
    }

    private fun isCheckBoxesChanged(): Boolean {
        return this.checkedBoxes.size > 0
    }

    private fun isChipsChanged(): Boolean {
        this.checkedChips = getFilters()
        return this.checkedChips.size > 0
    }

    private fun isLocationFiltersChanged(): Boolean {
        return this.locationFilters.size > 0
    }


    private fun isPriceRangeBarChanged(): Boolean {
        return activity_filter_price_rangebar.leftIndex * INTERVAL_PRICE != MIN_PRICE
                || activity_filter_price_rangebar.rightIndex * INTERVAL_PRICE != MAX_PRICE
    }

    private fun isSurfaceRangeBarChanged(): Boolean {
        return activity_filter_surface_rangebar.leftIndex * INTERVAL_SURFACE != MIN_SURFACE
                || activity_filter_surface_rangebar.rightIndex * INTERVAL_SURFACE != MAX_SURFACE
    }

    private fun isRoomRangeBarChanged(): Boolean {
        return activity_filter_room_rangebar.leftIndex * INTERVAL_ROOM != MIN_ROOM
                || activity_filter_room_rangebar.rightIndex * INTERVAL_ROOM != MAX_ROOM
    }

    // Inflate menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_filter_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Action on Toolbar button click
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.activity_filter_valid_btn -> {

            val intent = Intent()

            if (!this.activity_filter_search_view.query.isNullOrBlank()){
                intent.putExtra("query", this.activity_filter_search_view.query.toString())
                Log.d("query", this.activity_filter_search_view.query.toString())
            }

            if(this.isSurfaceRangeBarChanged()) {
               intent.putExtra("min_surface", activity_filter_surface_rangebar.leftIndex * INTERVAL_SURFACE)
               intent.putExtra("max_surface", activity_filter_surface_rangebar.rightIndex * INTERVAL_SURFACE)
            }

            if(this.isPriceRangeBarChanged()) {
                intent.putExtra("min_price", activity_filter_price_rangebar.leftIndex * INTERVAL_PRICE)
                intent.putExtra("max_price", activity_filter_price_rangebar.rightIndex * INTERVAL_PRICE)
            }

            if(this.isRoomRangeBarChanged()) {
                intent.putExtra("min_room", activity_filter_room_rangebar.leftIndex * INTERVAL_ROOM)
                intent.putExtra("max_room", activity_filter_room_rangebar.rightIndex * INTERVAL_ROOM)
            }

            if(this.isCheckBoxesChanged()) {
                intent.putExtra("category_array", this.checkedBoxes)
                Log.d("category_array", this.checkedBoxes.toString())
            }

            if(this.isChipsChanged()) {
                intent.putExtra("filter_array", this.checkedChips)
                Log.d("filter_array", this.checkedChips.toString())
            }

            if(this.isLocationFiltersChanged()){
                intent.putExtra("location_array", this.locationFilters)
                Log.d("location_array", this.locationFilters.toString())
            }

            setResult(Activity.RESULT_OK, intent)
            this.finish()

            true
        }
        else -> { super.onOptionsItemSelected(item) }
    }

    private fun addChip(city: String){
        // Init Chip view
        val chip = Chip(this)
        var cityStr = ""

        // Remove accent "'" from city name -> cause error in SQL
        cityStr = if (city.contains("\'")) {
            city.replace("\'", " ")
        }
        else { city }

        // Set Chip Close Icon
        chip.closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_mtrl_chip_close_circle)
        chip.isCloseIconVisible = true

        // Set Chip CloseIconClickListerner -> to suppress chip from chip group and locationFilters list
        chip.setOnCloseIconClickListener {
            this.locationFilters.remove(chip.tag)
            activity_filter_container_chip_group.removeView(chip)
            Log.d("LocationFilters -> sup", "$locationFilters")
        }

        // Set Text and Tag
        chip.text = city
        chip.tag = cityStr

        // Add City value to locationFilters list
        this.locationFilters.add(cityStr)
        Log.d("LocationFilters -> add", "$locationFilters")

        // Add Chip to Chip group container
        activity_filter_container_chip_group.addView(chip)
    }


    // Trigger search autocomplete
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            AUTOCOMPLETE_RQ ->
                when(resultCode) {
                    AutocompleteActivity.RESULT_OK -> {
                        Log.d(this.javaClass.simpleName, "RESULT OK")

                        if (Autocomplete.getPlaceFromIntent(data!!).addressComponents != null) {
                            val city = AddressCompenentsHelper(Autocomplete.getPlaceFromIntent(data).addressComponents?.asList()).parseCity()

                            if (city != null) { this.addChip(city) }
                            else { Toast.makeText(this, getString(R.string.autocomplete_city_error), Toast.LENGTH_LONG).show() }
                        }
                    }

                    AutocompleteActivity.RESULT_ERROR -> {
                        Log.e(this.javaClass.simpleName, Autocomplete.getStatusFromIntent(data!!).statusMessage)
                    }

                    AutocompleteActivity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "RESULT CANCELED")
                    }
                }
            else ->  super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
