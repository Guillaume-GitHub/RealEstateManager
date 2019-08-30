package com.openclassrooms.realestatemanager.controller

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.*
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.adapter.AgentArrayAdapter
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.model.entity.Locality
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_dialog_pick_image_choice.view.*
import kotlinx.android.synthetic.main.fragment_new_estate.*
import kotlinx.android.synthetic.main.nearby_chip_filters.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewEstateFragment : Fragment(), OnDeleteImageButtonClick {

    companion object {
        private const val CAMERA_REQUEST_CODE = 20
        private const val IMAGE_GALLERY_REQUEST_CODE = 30
        private const val PERMISSION_REQUEST_CODE = 40
        private const val MAPS_REQUEST_CODE = 50
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ItemImageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var imagesUriList = ArrayList<Uri>()
    private val agentList = ArrayList<Agent>()
    private lateinit var estateViewModel: EstateViewModel
    private lateinit var photoPatch: String
    private lateinit var agentAdapter: AgentArrayAdapter
    private var agentSelected: Agent? = null
    private var estateLocality: Locality? = null
    private var currentEstate: Estate? = null
    private var isModification = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.configureViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.retainInstance = true
        this.initView()
        this.configRecyclerView()
        // Control toolbar action menu in fragment
        this.setHasOptionsMenu(true)

        if (arguments?.getLong("estate_id") != null) {
            val id = arguments!!.getLong("estate_id")
            this.estateViewModel.getEstate(id)?.observe(this, androidx.lifecycle.Observer { estate ->
                this.currentEstate = estate
                this.isModification = true
                this.bindAll(estate)
            })
        }
    }

    //TODO : MANAGE NULL VALUES + FILL ALL VIEWS
    //fill views
    private fun bindAll(estate: Estate) {
        //Images
        this.imagesUriList.addAll(estate.images)
        this.imageAdapter.notifyDataSetChanged()
        // Text View
        fragment_new_estate_input_text_title.setText((estate.title))
        fragment_new_estate_input_text_description.setText((estate.description))
        fragment_new_estate_input_text_type.setText((estate.category))
        fragment_new_estate_input_text_surface.setText(("${estate.surface}"))
        fragment_new_estate_input_text_rooms.setText(("${estate.nbRoom}"))
        fragment_new_estate_input_autocomplete_location.setText((estate.locality.formattedAddress))
        fragment_new_estate_input_text_price.setText(("${estate.price}"))
        fragment_new_estate_input_text_agent.setText(("${estate.agent.name} ${estate.agent.surname}"))
        // Chip
        this.checkChips(estate.filters)
        //Locality
        this.estateLocality = estate.locality
        //Agent
        this.agentSelected = estate.agent
        // card_view_sale visibility
        card_view_sale.visibility = View.VISIBLE
    }

    private fun configRecyclerView() {
        this.recyclerView = fragment_new_estate_recycler_view_image
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = this.layoutManager
        this.imageAdapter = ItemImageAdapter(imagesUriList, this)
        this.recyclerView.adapter = imageAdapter
        PagerSnapHelper().attachToRecyclerView(this.recyclerView)
    }

    // Delete image in recycler
    override fun onDeleteButtonClick(position: Int) {
        try {
            this.imagesUriList.remove(this.imageAdapter.getItem(position))
            this.imageAdapter.notifyDataSetChanged()
        }
        catch (error: IndexOutOfBoundsException){
            Log.w("Delete image error : ", error.printStackTrace().toString())
        }
    }

    //************************************ ACTIVITY RESULT *************************************//
    // Get PlacesAutocomplete Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            IMAGE_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    if(data != null){
                        val path = Utils.getRealPathFromURI(context,data.data)
                        val uri = Uri.parse(path)
                        imagesUriList.add(uri)
                        imageAdapter.notifyDataSetChanged()
                        Log.d("IMAGES :",imagesUriList.toString())
                    }
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    imagesUriList.add(Uri.parse(photoPatch))
                    imageAdapter.notifyDataSetChanged()
                    Log.d("IMAGES :",imagesUriList.toString())
                }
            }

            MAPS_REQUEST_CODE -> {
                when(resultCode) {
                    RESULT_OK -> {
                        if (data != null) {
                            val streetNumber = data.getStringExtra("street_number")
                            val route = data.getStringExtra("route")
                            val postalCode = data.getStringExtra("postal_code")
                            val city = data.getStringExtra("locality")
                            val country = data.getStringExtra("country")
                            val formattedAddress = data.getStringExtra("formatted_address")
                            val latLng = data.getDoubleArrayExtra("latlng")

                            if (city != null && route != null && formattedAddress != null && country != null && latLng != null) {
                                this.estateLocality = Locality(0, formattedAddress, streetNumber, route, postalCode, city, country, LatLng(latLng[0], latLng[1]))
                                val address = this.estateLocality
                                fragment_new_estate_input_autocomplete_location.setText(address?.formattedAddress)
                            } else {
                                fragment_new_estate_input_layout_location.error = resources.getString(R.string.form_error_format_address)
                            }
                        }
                        else {
                            Toast.makeText(context,getString(R.string.result_error),Toast.LENGTH_SHORT).show()
                        }
                    }

                    RESULT_CANCELED -> {
                        Toast.makeText(context,getString(R.string.result_cancel),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initView() {

        //********************** card_view_sale *****************************//
        card_view_sale.visibility = View.GONE

        //********************** fragment_new_estate_input_autocomplete_location  *****************************//
        // Get focus on autocomplete location
        fragment_new_estate_input_autocomplete_location.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> if (hasFocus) startActivityForResult(Intent(context,MapsActivityForResult::class.java), MAPS_REQUEST_CODE) }
        fragment_new_estate_input_autocomplete_location.setOnClickListener(View.OnClickListener {startActivityForResult(Intent(context,MapsActivityForResult::class.java), MAPS_REQUEST_CODE)})

        //********************** fragment_new_estate_floating_btn  *****************************//
        fragment_new_estate_floating_btn.setOnClickListener(View.OnClickListener { v: View? -> this.showAddingImageDialog() })

        //********************** fragment_new_estate_input_text_type  *****************************//
        val autoCompleteTextView = view!!.findViewById<AutoCompleteTextView>(R.id.fragment_new_estate_input_text_type)
        val typeAdapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, resources.getStringArray(R.array.categoryArray))
        autoCompleteTextView.setAdapter(typeAdapter)

        autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){ autoCompleteTextView.showDropDown() }
        }
        autoCompleteTextView.setOnClickListener(View.OnClickListener {
            autoCompleteTextView.showDropDown()
        })

        //********************** fragment_new_estate_input_text_agent  *****************************//
        val userAutoComplete = view!!.findViewById<AutoCompleteTextView>(R.id.fragment_new_estate_input_text_agent)
        this.agentAdapter = AgentArrayAdapter(context!!, R.layout.spinner_dropdown_user, agentList)
        this.getAgents()
        userAutoComplete.setAdapter(agentAdapter)
            /*Focus change Listener*/
        userAutoComplete.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->  if(hasFocus) userAutoComplete.showDropDown() }
            /*Click Listener*/
        userAutoComplete.setOnClickListener(View.OnClickListener { userAutoComplete.showDropDown() })
        userAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val agent = parent.adapter.getItem(position) as Agent
            this.agentSelected = agent
            userAutoComplete.setText("${agent.name} ${agent.surname}")
        }
    }

    private fun showAddingImageDialog() {
        if (this.checkPermission()) {
            // Create a builder
            val builder = AlertDialog.Builder(context!!)
            // Inflate custom alert dialog view
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog_pick_image_choice, null)
            // Set dialog Title and custom view
            builder.setTitle(getString(R.string.alert_dialog_pick_image_title)).setView(dialogLayout)
            // Create and Show dialog
            val dialog = builder.create()
            dialog.show()
            // Set Click listener on button's custom view
            // Start camera intent
            dialogLayout.alert_dialog_image_choice_camera.setOnClickListener(View.OnClickListener { v: View? ->
                this.captureWithCamera()
                dialog.dismiss()
            })
            // Start Image Gallery intent
            dialogLayout.alert_dialog_image_choice_gallery.setOnClickListener(View.OnClickListener { v: View? ->
                this.pickImageGallery()
                dialog.dismiss()
            })
        }
        else {
            this.requestPermission()
        }
    }

    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoPatch = absolutePath
        }
    }

    // Create intent and start activity to pick image from gallery
    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("image/*")
        val mimeTypes: Array<String> = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        startActivityForResult(intent, IMAGE_GALLERY_REQUEST_CODE)
    }

    // Create intent and start activity to pick image from camera
    private fun captureWithCamera() {
        val uri = FileProvider.getUriForFile(context!!,"com.openclassrooms.realestatemanager.fileprovider",createFile())
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE).putExtra(MediaStore.EXTRA_OUTPUT,uri), CAMERA_REQUEST_CODE)
    }

    // Check Camera And Storage Permissions
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // Display Permissions dialog to users
    private fun requestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
    }

    // Catch requestPermission() result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    this.showAddingImageDialog()

                } else { Toast.makeText(context,getString(R.string.permission_denied),Toast.LENGTH_SHORT).show() }
                return
            }
            else -> { }
        }
    }

    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory(context!!)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    // Action on Toolbar button click
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.activity_new_estate_valid_btn -> {
            this.cleanError()
            this.validFrom()

            if (isFormValid()) { this.saveForm() }
            true
        }
        else -> { super.onOptionsItemSelected(item) }
    }

    //Display Error messages
    private fun validFrom() {
        if (fragment_new_estate_input_text_title.text == null || fragment_new_estate_input_text_title.text!!.length < 3) {
            fragment_new_estate_input_layout_title.error = resources.getString(R.string.form_error_characters_3)
        }

        if (fragment_new_estate_input_text_type.text.isNullOrBlank()) {
            fragment_new_estate_input_layout_type.error = resources.getString(R.string.form_error_type)
        }

        if (fragment_new_estate_input_text_surface.text.isNullOrBlank() || fragment_new_estate_input_text_surface.text.toString().toInt() <= 0) {
            fragment_new_estate_input_layout_surface.error = resources.getString(R.string.form_error_surface)
        }

        if (fragment_new_estate_input_text_price.text.isNullOrBlank()) {
            fragment_new_estate_input_layout_price.error = resources.getString(R.string.form_error_price)
        }

        if (fragment_new_estate_input_text_rooms.text.isNullOrBlank() || fragment_new_estate_input_text_rooms.text.toString().toInt() <= 0) {
            fragment_new_estate_input_layout_rooms.error = resources.getString(R.string.form_error_rooms)
        }

        if (fragment_new_estate_input_autocomplete_location.text.isNullOrBlank() || this.estateLocality == null){
            fragment_new_estate_input_layout_location.error = resources.getString(R.string.form_error_not_geolocated)
        }

        if (fragment_new_estate_input_text_description.text.isNullOrBlank() || fragment_new_estate_input_text_description.text!!.length < 20){
            fragment_new_estate_input_layout_description.error = resources.getString(R.string.form_error_characters_20)
        }

        if (fragment_new_estate_input_text_agent.text.isNullOrBlank() || this.agentSelected == null){
            fragment_new_estate_input_layout_agent.error = resources.getString(R.string.form_error_agent)
        }

        if (this.imagesUriList.size < 1){
            fragment_new_estate_image_error.apply {
                text = resources.getString(R.string.form_error_images)
                visibility = View.VISIBLE
            }
        }
    }

    // Clean error message for all input layout
    private fun cleanError() {
        fragment_new_estate_input_layout_title.error = null
        fragment_new_estate_input_layout_type.error = null
        fragment_new_estate_input_layout_surface.error = null
        fragment_new_estate_input_layout_price.error = null
        fragment_new_estate_input_layout_rooms.error = null
        fragment_new_estate_input_layout_location.error = null
        fragment_new_estate_input_layout_description.error = null
        fragment_new_estate_input_layout_agent.error = null
        fragment_new_estate_image_error.apply {
            text = null
            visibility = View.GONE
        }
    }

    // Check if Form is valid
    private fun isFormValid(): Boolean {
        return fragment_new_estate_input_layout_title.error.isNullOrBlank()
                && fragment_new_estate_input_layout_type.error.isNullOrBlank()
                && fragment_new_estate_input_layout_surface.error.isNullOrBlank()
                && fragment_new_estate_input_layout_price.error.isNullOrBlank()
                && fragment_new_estate_input_layout_rooms.error.isNullOrBlank()
                && fragment_new_estate_input_layout_location.error.isNullOrBlank()
                && fragment_new_estate_input_layout_description.error.isNullOrBlank()
                && fragment_new_estate_input_layout_agent.error.isNullOrBlank()
                && this.imagesUriList.size > 0
    }

    private fun saveForm() {
        when(true){
            isModification -> {
                if(this.estateLocality == currentEstate?.locality){
                    this.updateEstate()
                }
            }
            else -> {
                this.insertLocality()
            }
        }
    }

    // Create new Locality
    private fun insertLocality(){
        val locality = this.estateLocality

        if (locality != null) {
            @Suppress
            this.estateViewModel.insertLocality(locality).subscribeOn(Schedulers.newThread()).subscribe(
                    { localityId -> getLocality(localityId) },
                    { error -> Log.w(this.javaClass.simpleName, "insertLocality() OnError : ${error.printStackTrace()}") })
        }
        else {
            fragment_new_estate_input_layout_location.error = resources.getString(R.string.form_error_format_address)
        }
    }

    // Get Last Locality Inserted
    private fun getLocality(id: Long){
        @Suppress
        this.estateViewModel.getObservableLocality(id).subscribeOn(Schedulers.newThread()).subscribe(
                { locality -> this.insertEstate(locality) }, // onNext
                { error -> Log.w(this.javaClass.simpleName,"getLocality() OnError : ${error.printStackTrace()}") }) // onError
    }

    // Get List of Agent
    private fun getAgents(){
        this.estateViewModel.getAgents()?.observe(this,androidx.lifecycle.Observer { agents ->
            if(this.agentList.addAll(agents)) this.agentAdapter.notifyDataSetChanged()
        })
    }

    // Create nex estate and insert it in database
   private fun insertEstate(locality: Locality){
       try {
           val estate = Estate(0, // ID = 0 -> auto Increment
                   fragment_new_estate_input_text_type.text.toString(), // Category
                   fragment_new_estate_input_text_title.text.toString(), // Title
                   fragment_new_estate_input_text_description.text.toString(), // Description
                   fragment_new_estate_input_text_surface.text.toString().toInt(), // Surface
                   fragment_new_estate_input_text_rooms.text.toString().toInt(), // Nb of Rooms
                   fragment_new_estate_input_text_price.text.toString().toLong(), // Price
                   Calendar.getInstance().time, // Published Date = Current date when insert
                   null, // Sale Date Always Null with when new insert in database
                   this.getFilters(), // Get Filters
                   this.imagesUriList, // Save Uri images
                   locality, // Locality = Locality.class Object (Not null)
                   this.agentSelected!!) // Agent

           // Insert in database and Observe result
           @Suppress
           this.estateViewModel.insertEstate(estate).subscribe(
                   { this.notifyUserAndQuit() }, // OnSuccess
                   { error -> Log.w(this.javaClass.simpleName,"OnError : ${error.printStackTrace()}") }
           )
       }
       catch (error: NullPointerException) { Log.w(this.javaClass.simpleName, error) }
    }

    private fun updateEstate(){
        try {
            val estate = this.currentEstate!!
            estate.category = fragment_new_estate_input_text_type.text.toString()
            estate.title = fragment_new_estate_input_text_title.text.toString()
            estate.description = fragment_new_estate_input_text_description.text.toString()
            estate.surface = fragment_new_estate_input_text_surface.text.toString().toInt()
            estate.nbRoom = fragment_new_estate_input_text_rooms.text.toString().toInt()
            estate.price = fragment_new_estate_input_text_price.text.toString().toLong()
            estate.filters = this.getFilters()
            estate.images = this.imagesUriList
            estate.agent = this.agentSelected!!

            // Update Estate and Observe result
            @Suppress
            this.estateViewModel.updateEstate(estate).subscribeOn(Schedulers.newThread()).subscribe(
                    { this.notifyUserAndQuit() }, // OnSuccess
                    { error -> Log.w(this.javaClass.simpleName,"OnError : ${error.printStackTrace()}") }
            )
        }
        catch (error: NullPointerException) { Log.w(this.javaClass.simpleName, error) }
    }

    // Return an Array of Filters Tag
    // Get TAGS with FiltersHelper::class
    private fun getFilters(): ArrayList<String>? {
        val filterList = ArrayList<String>()
        if (nearby_chip_hospital.isChecked) filterList.add(FiltersHelper.HEALTH_TAG)
        if (nearby_chip_restaurant.isChecked) filterList.add(FiltersHelper.RESTAURANT_TAG)
        if (nearby_chip_school.isChecked) filterList.add(FiltersHelper.SCHOOL_TAG)
        if (nearby_chip_store.isChecked) filterList.add(FiltersHelper.STORE_TAG)
        if (nearby_chip_supermarket.isChecked) filterList.add(FiltersHelper.SUPERMARKET_TAG)
        if (nearby_chip_transport.isChecked) filterList.add(FiltersHelper.TRANSPORT_TAG)
        if (nearby_chip_sport.isChecked) filterList.add(FiltersHelper.SPORT_TAG)

        return if (filterList.size > 0) filterList else null
    }

    private fun checkChips(filterList:ArrayList<String>?){
        filterList?.forEach { filter ->
            when(filter){
                FiltersHelper.HEALTH_TAG -> nearby_chip_hospital.isChecked
                FiltersHelper.RESTAURANT_TAG -> nearby_chip_restaurant.isChecked
                FiltersHelper.SCHOOL_TAG -> nearby_chip_school.isChecked
                FiltersHelper.SUPERMARKET_TAG -> nearby_chip_supermarket.isChecked
                FiltersHelper.STORE_TAG -> nearby_chip_store.isChecked
                FiltersHelper.TRANSPORT_TAG -> nearby_chip_transport.isChecked
                FiltersHelper.SPORT_TAG -> nearby_chip_sport.isChecked
                else -> {}
            }
        }
    }

    private fun notifyUserAndQuit(){
        NotificationHelper.sendNotification(context!!)
        this.activity?.finish()
    }
}

