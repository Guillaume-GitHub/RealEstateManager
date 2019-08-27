package com.openclassrooms.realestatemanager.controller

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.ComponentsHelper
import com.openclassrooms.realestatemanager.api.GeocodingServiceBuilder
import com.openclassrooms.realestatemanager.model.Component
import com.openclassrooms.realestatemanager.model.ReverseGeocodingAddress
import com.openclassrooms.realestatemanager.model.ReverseGeocodingResult
import com.openclassrooms.realestatemanager.model.entity.Locality
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivityForResult : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener, SingleObserver<ReverseGeocodingResult> {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var disposable: Disposable
    private lateinit var estateAddress: ReverseGeocodingAddress
    private lateinit var estatePosition: LatLng

    // Create constant request code
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val CHECK_SETTINGS_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_for_result)

        // Create Toolbar
        this.configToolbar()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Init FusedLocation services
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        // Check and ask permission if they are denied
        this.setUpAccessLocationPermissions()
        this.setUpLocationService()
        map.setOnMapLongClickListener(this)
    }

    //Configure toolbar and navigation
    private fun configToolbar(){
        // Set the toolbar as support action bar
        setSupportActionBar(activity_maps_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    //************************ PERMISSIONS CALLBACK ************************//
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
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    this.getAddress(currentLatLng)
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

    // Add marker on map and fetch address
    private fun addMarker(position: LatLng, title: String?){
        map.clear()
        map.addMarker(MarkerOptions().position(position)?.title(title))
        this.getAddress(position)
    }

    // Fetch address from LatLng
    private fun getAddress(position: LatLng){
        this.estatePosition = position
        GeocodingServiceBuilder.getAddressFromLatLng(position).subscribe(this)
    }

    // Check Location service enable
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

    //************************ MAP LONG CLICK CALLBACK ************************//
    override fun onMapLongClick(position: LatLng?) {
        Log.d(this.javaClass.simpleName,"[onMapLongClick] Lat : ${position?.latitude}, Long : ${position?.longitude}")
        if(position != null){
            this.addMarker(position,null)
            this.estatePosition = position
        }
    }

    //************************ GET ADDRESS CALLBACK ************************//

    override fun onSuccess(results: ReverseGeocodingResult) {
        Log.e(this.javaClass.simpleName, "[onSuccess]")

        if (results.address.isNotEmpty()){
            this.estateAddress = results.address[0]
            Log.d("Address ", results.address[0].formattedAddress)
            showAddress(results.address[0].formattedAddress!!)
        }
        this.disposable.dispose()
    }

    override fun onSubscribe(d: Disposable) {
        Log.e(this.javaClass.simpleName, "[onSubscribe]")
        this.disposable = d
    }

    override fun onError(e: Throwable) {
        Log.e(this.javaClass.simpleName, "[onError]")
        Log.e(this.javaClass.simpleName, e.toString())
    }

    // Display the fetched address in snackbar
    private fun showAddress(address: String){
        val view = findViewById<RelativeLayout>(R.id.activity_maps_for_result_root)
        val snackBar = Snackbar.make(view, address , Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Valid", View.OnClickListener {
            this.returnResult(this.estateAddress)
        })
        snackBar.show()
    }

    // PUt Extra Values
    private fun returnResult(result : ReverseGeocodingAddress) {

        if (result.addressComponents != null) {

            val addressComponentList = result.addressComponents

            if (addressComponentList != null && addressComponentList.isNotEmpty()) {

                val componentList: List<Component> = addressComponentList
                val addressParser = ComponentsHelper(componentList)
                val intent = Intent()

                val streetNumber = addressParser.parseStreetNumber()
                val route = addressParser.parseRoute()
                val postalCode = addressParser.parsePostalCode()
                val city =  addressParser.parseCity()
                val country = addressParser.parseCountry()
                val formattedAddress = result.formattedAddress
                val latLng = doubleArrayOf(result.geometry?.location?.lat!!,result.geometry?.location?.lng!!)

                intent.putExtra("street_number", streetNumber)
                Log.d("[street_number]", streetNumber)
                intent.putExtra("route", route)
                Log.d("[route]", route)
                intent.putExtra("postal_code", postalCode)
                Log.d("[postal_code]", postalCode)
                intent.putExtra("locality", city)
                Log.d("[locality[", city)
                intent.putExtra("country", country)
                Log.d("[country]", country)
                intent.putExtra("formatted_address", formattedAddress)
                Log.d("[formatted_address]", formattedAddress)
                intent.putExtra("latlng", latLng)
                Log.d("[latlng]", latLng.toString())

                setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
        }
        else {
            setResult(Activity.RESULT_OK,null)
            this.finish()
        }
    }
}
