package com.openclassrooms.realestatemanager.controller

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.BottomSheetImageAdapter
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.maps_bottom_sheet.view.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var viewModel: EstateViewModel
    private lateinit var estateList: List<Estate>

    // Create constant request code
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val CHECK_SETTINGS_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Configure viewModel
        this.configureViewModel()
        // Configure the toolbar
        this.configToolbar()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    // Config view model
    private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(this)
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        // Check and ask permission if they are denied
        this.setUpAccessLocationPermissions()
        this.setUpLocationService()
        this.getEstates()
    }

    //Configure toolbar and navigation
    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_maps_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    // Check if permissions are Granted and request permission if Denied
    private fun setUpAccessLocationPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        else {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
                if(location != null){
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20f))
                    lastLocation = location
                }
            })
        }
    }

    // Check permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE ->
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    this.setUpAccessLocationPermissions()
                }
            }
        return
    }


    // Add marker on map
    private fun addMarker(position: LatLng, index: Int){
        val marker = MarkerOptions().position(position)
        marker.zIndex(index.toFloat())
        map.addMarker(marker)
    }

    // Trigger Marker click and show bottom sheet dialog
    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.zIndex != null){
            val dialogView = layoutInflater.inflate(R.layout.maps_bottom_sheet, null)
            this.bindEstate(dialogView, marker.zIndex.toInt())
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(dialogView)
            dialog.show()
        }
        return true
    }

    // Check service Location enable
    private fun setUpLocationService() {
        // Create location request
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Build Location settings request & check settings
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        // Check  Location settings
        task.addOnFailureListener { error ->
            if (error is ResolvableApiException) {
                // Location settings are disable -> showing the user a dialog
                try {
                    // Show Location service dialog
                    error.startResolutionForResult(this, CHECK_SETTINGS_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Do nothing
                }
            }
        }
    }

    // Fetch List of estate In database
    private fun getEstates(){
        this.viewModel.getAllEstates()?.observe(this, Observer { estateList ->
            this.estateList = estateList
            estateList.forEachIndexed { index, estate ->
                this.addMarker(estate.locality.latLng, index)
            }
        })
    }

    // Bind Bottom sheet view with estate's details
    private fun bindEstate(dialogView: View ,estatePosition: Int){
        val estate = this.estateList[estatePosition]

        val recycler = dialogView.maps_bottom_sheet_recycler
        recycler.adapter = BottomSheetImageAdapter(estate.images)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        LinearSnapHelper().attachToRecyclerView(recycler)

        dialogView.maps_bottom_sheet_title.text = estate.title

        if(estate.saleDate == null){
            dialogView.maps_bottom_sheet_badge_for_sale.visibility = View.VISIBLE
            dialogView.maps_bottom_sheet_date.text = estate.publishedDate.toString()
        }
        else{
            dialogView.maps_bottom_sheet_badge_sale.visibility = View.VISIBLE
            dialogView.maps_bottom_sheet_date.text = estate.saleDate.toString()
        }

        dialogView.maps_bottom_sheet_price.text = estate.price.toInt().toString()
        dialogView.maps_bottom_sheet_address.text = estate.locality.formattedAddress
        dialogView.maps_bottom_sheet_type.text = estate.category
        dialogView.maps_bottom_sheet_surface.text = "${estate.surface} m²"
        dialogView.maps_bottom_sheet_rooms.text = "${estate.nbRoom} rooms"
        dialogView.maps_bottom_sheet_description.text = estate.description
        dialogView.maps_bottom_sheet_agent.text = " ${estate.agent.name} ${estate.agent.surname}"

        dialogView.maps_bottom_sheet_agent.apply {

        }

        dialogView.maps_bottom_sheet_agent_image.apply {
            if (estate.agent.image != null) setImageURI(estate.agent.image)
            else background = resources.getDrawable(R.mipmap.ic_launcher_round)
        }
    }

}
