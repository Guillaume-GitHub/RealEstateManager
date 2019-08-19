package com.openclassrooms.realestatemanager.controller

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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.*
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.adapter.AgentArrayAdapter
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Locality
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_dialog_pick_image_choice.view.*
import kotlinx.android.synthetic.main.fragment_new_estate.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewEstateFragment(val estate: Estate? = null) : Fragment(), OnDeleteImageButtonClick {

    override fun onDeleteButtonClick(position: Int) {
        try {
            Log.d("CLICK DELETE BTN", "-> Work")
            this.imagesUriList.remove(this.imageAdapter.getItem(position))
            this.imageAdapter.notifyItemRemoved(position)
            Log.d("IMAGE LIST",  imagesUriList.toString())
        }
        catch (error: IndexOutOfBoundsException){
            Log.w("Delete image error : ", error.printStackTrace().toString())
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 10
        private const val CAMERA_REQUEST_CODE = 20
        private const val IMAGE_GALLERY_REQUEST_CODE = 30
        private const val PERMISSION_REQUEST_CODE = 40
    }

    // Type of fields return in Place Object
    private val fields: List<Place.Field> = listOf(Place.Field.ADDRESS,Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ItemImageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val imagesUriList = ArrayList<Uri>()
    private val agentList = ArrayList<Agent>()
    private lateinit var estateViewModel: EstateViewModel
    private var place: Place? = null
    private lateinit var addressParser: AddressComponentsHelper
    private lateinit var photoPatch: String
    private lateinit var agentAdapter: AgentArrayAdapter
    private var agentSelected: Agent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.estate != null) this.bind(estate)

        this.configureViewModel()
        this.initView()
        this.configRecyclerView()
        // Control toolbar action menu in fragment
        this.setHasOptionsMenu(true)
    }

    //TODO : MANAGE NULL VALUES + FILL ALL VIEWS
    //fill views
    private fun bind(estate: Estate) {
        fragment_new_estate_input_text_title.setText((estate.title))
        fragment_new_estate_input_text_type.setText((estate.category))
        fragment_new_estate_input_text_price.setText((estate.price.toString()))
        fragment_new_estate_input_autocomplete_location.setText((estate.address))
    }

    private fun configRecyclerView() {
        this.recyclerView = fragment_new_estate_recycler_view_image
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = this.layoutManager
        this.imageAdapter = ItemImageAdapter(imagesUriList, this)
        this.recyclerView.adapter = imageAdapter
        PagerSnapHelper().attachToRecyclerView(this.recyclerView)
    }

    // Start PlacesAutocomplete activity -> get result in onActivityResult()
    private fun startPlacesAutocomplete() {
        // Init Places with api KEY
        Places.initialize(context!!, getString(R.string.google_maps_key))

        // Create a new Places client instance.
        val placesClient = Places.createClient(context!!)

        // Create autocomplete intent with display mode
        val typeFiler = TypeFilter.ADDRESS
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(typeFiler).build(context!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    // Get PlacesAutocomplete Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AUTOCOMPLETE_REQUEST_CODE ->
                when (resultCode) {
                    AutocompleteActivity.RESULT_OK -> {

                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT OK")

                        if (Autocomplete.getPlaceFromIntent(data!!).address != null) {
                            fragment_new_estate_input_autocomplete_location.setText(Autocomplete.getPlaceFromIntent(data).address)
                            place = Autocomplete.getPlaceFromIntent(data)
                        }
                    }

                    AutocompleteActivity.RESULT_ERROR -> {
                        Log.e(this.javaClass.simpleName, Autocomplete.getStatusFromIntent(data!!).statusMessage)
                    }

                    AutocompleteActivity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT CANCELED")
                    }
                }

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
        }
    }

    private fun initView() {

        //********************** fragment_new_estate_input_autocomplete_location  *****************************//
        // Get focus on autocomplete location
        fragment_new_estate_input_autocomplete_location.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> if (hasFocus) this.startPlacesAutocomplete() }
        fragment_new_estate_input_autocomplete_location.setOnClickListener(View.OnClickListener { this.startPlacesAutocomplete() })

        //********************** fragment_new_estate_floating_btn  *****************************//
        fragment_new_estate_floating_btn.setOnClickListener(View.OnClickListener { v: View? ->
            this.showAddingImageDialog()
        })

        //********************** fragment_new_estate_input_text_type  *****************************//
        val autoCompleteTextView = view!!.findViewById<AutoCompleteTextView>(R.id.fragment_new_estate_input_text_type)
        val typeAdapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, resources.getStringArray(R.array.categoryArray))
        autoCompleteTextView.setAdapter(typeAdapter)

        autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> if (hasFocus) autoCompleteTextView.showDropDown() }
        autoCompleteTextView.setOnClickListener(View.OnClickListener { autoCompleteTextView.showDropDown() })

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
            userAutoComplete.setText(agent.name + " ${agent.surname}")
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

        if (fragment_new_estate_input_text_surface.text.isNullOrBlank()) {
            fragment_new_estate_input_layout_surface.error = resources.getString(R.string.form_error_surface)
        }

        if (fragment_new_estate_input_text_price.text.isNullOrBlank()) {
            fragment_new_estate_input_layout_price.error = resources.getString(R.string.form_error_price)
        }

        if (fragment_new_estate_input_text_rooms.text.isNullOrBlank()) {
            fragment_new_estate_input_layout_rooms.error = resources.getString(R.string.form_error_rooms)
        }

        if (fragment_new_estate_input_autocomplete_location.text.isNullOrBlank()){
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
        this.addressParser = AddressComponentsHelper(this.place!!)
        this.insertLocality()
    }

    // Create new Locality
    private fun insertLocality(){
        val postalCode = this.addressParser.parsePostalCode()
        val city = this.addressParser.parseCity()

        if (postalCode != null && city != null) {
            @Suppress
            this.estateViewModel.insertLocality(Locality(0, postalCode, city)).subscribeOn(Schedulers.newThread()).subscribe(
                    { localityId -> getLocality(localityId) },
                    { error -> Log.w(this.javaClass.simpleName, "OnError : ${error.printStackTrace()}") })
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
                { error -> Log.w(this.javaClass.simpleName,"OnError : ${error.printStackTrace()}") }) // onError
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
                   fragment_new_estate_input_autocomplete_location.text.toString(), // Address
                   fragment_new_estate_input_text_description.text.toString(), // Description
                   fragment_new_estate_input_text_price.text.toString().toLong(), // Price
                   Calendar.getInstance().time, // Published Date = Current date when insert
                   null, // Sale Date Always Null with when new insert in database
                   this.getFilters(), // Get Filters
                   this.imagesUriList, // Save Uri images
                   locality, // Locality = Locality.class Object (Not null)
                   this.place?.latLng!!, // Not Null LatLng Object
                   this.agentSelected!!) // Agent

           // Insert in database and Observe result
           @Suppress
           this.estateViewModel.insertEstate(estate).subscribe(
                   { this.activity?.finish() }, // OnSuccess
                   { error -> Log.w(this.javaClass.simpleName,"OnError : ${error.printStackTrace()}") }
           )
       }
       catch (error: NullPointerException) { Log.w(this.javaClass.simpleName, error) }
    }

    // Return an Array of Filters Tag
    // Get TAGS with FiltersHelper::class
    private fun getFilters(): ArrayList<String>? {
        val filterList = ArrayList<String>()
        if (fragment_new_estate_chip_hospital.isChecked) filterList.add(FiltersHelper.HEALTH_TAG)
        if (fragment_new_estate_chip_restaurant.isChecked) filterList.add(FiltersHelper.RESTAURANT_TAG)
        if (fragment_new_estate_chip_school.isChecked) filterList.add(FiltersHelper.SCHOOL_TAG)
        if (fragment_new_estate_chip_store.isChecked) filterList.add(FiltersHelper.STORE_TAG)
        if (fragment_new_estate_chip_supermarket.isChecked) filterList.add(FiltersHelper.SUPERMARKET_TAG)
        if (fragment_new_estate_chip_transport.isChecked) filterList.add(FiltersHelper.TRANSPORT_TAG)
        if (fragment_new_estate_chip_sport.isChecked) filterList.add(FiltersHelper.SPORT_TAG)

        return if (filterList.size > 0) filterList else null
    }
}

