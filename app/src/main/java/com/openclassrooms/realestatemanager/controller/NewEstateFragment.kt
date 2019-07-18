package com.openclassrooms.realestatemanager.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_new_estate.*

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.openclassrooms.realestatemanager.adapter.ItemCategoryAdapter
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.model.EstateImage
import kotlinx.android.synthetic.main.alert_dialog_pick_image_choice.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.recyclerview_image_item.*

class NewEstateFragment(val estate: Estate? = null) : Fragment(){


    companion object{
        private const val AUTOCOMPLETE_REQUEST_CODE = 10
        private const val CAMERA_REQUEST_CODE = 20
        private const val IMAGE_GALLERY_REQUEST_CODE = 30
    }

    // Type of fields return in Place Object
    private val fields : List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)

    private lateinit var imageUri : Uri
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ItemImageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val estateImageList = ArrayList<EstateImage>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.estate != null) this.bind(estate)

        this.initView()
        this.configRecyclerView()
    }

    //TODO : MANAGE NULL VALUES + FILL ALL VIEWS
    //fill views
    private fun bind(estate: Estate) {
        fragment_new_estate_input_text_title.setText((estate.title))
        fragment_new_estate_input_text_type.setText((estate.category))
        fragment_new_estate_input_text_price.setText((estate.price.toString()))
        fragment_new_estate_input_autocomplete_location.setText((estate.address))

    }

    private fun configRecyclerView(){
        this.recyclerView = fragment_new_estate_recycler_view_image
        this.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.layoutManager = this.layoutManager
        this.imageAdapter = ItemImageAdapter(estateImageList)
        this.recyclerView.adapter = imageAdapter


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

            IMAGE_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data!!.data
                    estateImageList.add(EstateImage(uri))
                    imageAdapter.notifyDataSetChanged()
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val bitmap = data!!.extras.get("data") as Bitmap
                    estateImageList.add(EstateImage(null, bitmap))
                    imageAdapter.notifyDataSetChanged()
                }
            }

        }
    }


    private fun initView(){

        //********************** fragment_new_estate_input_autocomplete_location  *****************************//
        // Get focus on autocomplete location
        fragment_new_estate_input_autocomplete_location.setOnFocusChangeListener(View.OnFocusChangeListener {
            v, hasFocus -> if(hasFocus) this.startPlacesAutocomplete()
        })


        //********************** fragment_new_estate_floating_btn  *****************************//
        fragment_new_estate_floating_btn.setOnClickListener(View.OnClickListener {
            v: View? -> this.showAddingImageDialog()

        })
    }

    private fun showAddingImageDialog(){
        // Create a builder
        val builder = AlertDialog.Builder(context!!)
        // Inflate custom alert dialog view
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog_pick_image_choice,null)

        // Set dialog Title and custom view
        builder.setTitle("Chose a photo").setView(dialogLayout)

        // Create and Show dialog
        val dialog = builder.create()
        dialog.show()

        // Set Click listener on button's custom view
        // Start camera intent
        dialogLayout.alert_dialog_image_choice_camera.setOnClickListener(View.OnClickListener {
            v: View? ->
            this.captureWithCamera()
            dialog.dismiss()
        })
        // Start Image Gallery intent
        dialogLayout.alert_dialog_image_choice_gallery.setOnClickListener(View.OnClickListener {
            v: View? ->
            this.pickImageGallery()
            dialog.dismiss()
        })
    }

    // Create intent and start activity to pick image from gallery
    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type =("image/*")
        val mimeTypes: Array<String> = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)

        startActivityForResult(intent, IMAGE_GALLERY_REQUEST_CODE)
    }
    // Create intent and start activity to pick image from camera
    private fun captureWithCamera(){
       startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE), CAMERA_REQUEST_CODE)
    }
}

