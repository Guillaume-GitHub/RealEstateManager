package com.openclassrooms.realestatemanager.controller

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.Injections.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.FiltersHelper
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.adapter.RecyclerIndicatorDecoration
import com.openclassrooms.realestatemanager.api.ApiServicesBuilder
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.Exception

class DetailFragment : Fragment(){

    private var imagesList = ArrayList<Uri>()
    private lateinit var adapter: ItemImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var currentEstate: Estate
    private var estateId: Long = -1
    private lateinit var viewModel: EstateViewModel
    private var isDollar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       this.setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle: Bundle = this.arguments!!
        this.estateId = bundle.getLong("estate_id")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.configureViewModel()
        this.recyclerViewItemConfig()
        this.configureToolbar()
        this.fetchEstate(estateId)
        this.configureFloatingEditBtn()
    }

    private fun configureToolbar(){
        when(true){
            resources.getBoolean(R.bool.isTabletMode),
            resources.getBoolean(R.bool.isTabletLandMode)-> {}
            else -> {
                fragment_detail_toolbar.setNavigationOnClickListener{ activity?.onBackPressed() }
                val activity = activity as AppCompatActivity
                activity.setSupportActionBar(fragment_detail_toolbar)
                activity.supportActionBar?.title = ""
            }
        }
    }

    private fun configureFloatingEditBtn(){
        fragment_detail_floating_btn.setOnClickListener {
            if (this.estateId > -1) {
                val intent = Intent(context, NewEstateActivity::class.java)
                intent.putExtra("estate_id", this.estateId)
                startActivity(intent)
            }
        }
    }

    private fun bind(estate: Estate) {
        // Images section
        this.addImagesToRecycler(estate.images)
        // Title section
        fragment_detail_title.text = estate.title
        fragment_detail_currency.text = "$"
        fragment_detail_price.text = estate.price.toString()

        fragment_detail_date.apply {
            if(estate.saleDate == null) {
                text = Utils.getFormattedDate(estate.publishedDate)
                fragment_detail_badge_for_sale.visibility = View.VISIBLE
                fragment_detail_badge_sale.visibility = View.GONE
            }
            else {
                text = Utils.getFormattedDate(estate.saleDate)
                fragment_detail_badge_for_sale.visibility = View.GONE
                fragment_detail_badge_sale.visibility = View.VISIBLE
            }
        }
        // Criteria section
        fragment_detail_type_text.text = estate.category
        fragment_detail_surface_text.text = this.getSurfaceText(estate)
        fragment_detail_room_text.text = this.getRoomText(estate)
        // Description section
        fragment_detail_description.text = estate.description
        fragment_detail_address.text = estate.locality.formattedAddress
        // Nearby section
        this.showNearbyPOI(estate.filters)
        // Posted By section
        fragment_detail_agent_name.text = this.getAgentText(estate)

        fragment_detail_agent_image.apply {
            if (estate.agent.image != null) setImageURI(estate.agent.image)
            else background = resources.getDrawable(R.mipmap.ic_launcher_round)
        }
    }

    private fun configureViewModel(){
        val viewModelFactory = Injection.provideViewModelFactory(context!!)
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    private fun fetchEstate(id: Long){
        this.viewModel.getEstate(id)!!.observe(this, Observer { estate ->
            if(estate != null) {
                this.currentEstate = estate
                this.bind( this.currentEstate)
                this.getStaticMap(estate.locality.latLng)
            }
            else { Log.w(this::class.java.simpleName, "Fail to get Estate")}
        })
    }

    private fun addImagesToRecycler(images: ArrayList<Uri>) {
        this.imagesList.clear()
        this.imagesList.addAll(images)
        this.adapter.notifyDataSetChanged()
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewItemConfig(){
        this.adapter = ItemImageAdapter(imagesList,null)
        this.recyclerView = fragment_detail_recycler_view
        this.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        this.recyclerView.adapter = this.adapter
        PagerSnapHelper().attachToRecyclerView(this.recyclerView)
        this.recyclerView.addItemDecoration(RecyclerIndicatorDecoration())
    }

    private fun getStaticMap(latLng: LatLng){
        val url = ApiServicesBuilder.getStaticMapUrl(latLng)
        Picasso.get().load(url).into(fragment_detail_static_map_image, object : Callback {

                    override fun onSuccess() {
                        fragment_detail_static_map_image.visibility = View.VISIBLE
                    }

                    override fun onError(e: Exception?) {
                        e?.printStackTrace()
                    }
                })
    }

    // Return Number of rooms text
    private fun getRoomText(estate: Estate): String {
        return if (estate.nbRoom > 1) estate.nbRoom.toString() + " Rooms"  else estate.nbRoom.toString() + " Room"
    }

    // Return estate Surface
    private fun getSurfaceText(estate: Estate): String {
        return estate.surface.toString() + " m²"
    }

    // Return Agent name
    private fun getAgentText(estate: Estate): String {
        return estate.agent.name + " ${estate.agent.surname}"
    }

    // Display Nearby section
    private fun showNearbyPOI(filtersList: ArrayList<String>?){
        fragment_detail_nearby_chip_group.removeAllViews()
        if(filtersList != null) {
            filtersList.forEach { tag ->
                when (tag) {
                    FiltersHelper.SCHOOL_TAG -> this.addChip(FiltersHelper.SCHOOL_TAG)
                    FiltersHelper.HEALTH_TAG -> this.addChip(FiltersHelper.HEALTH_TAG)
                    FiltersHelper.RESTAURANT_TAG -> this.addChip(FiltersHelper.RESTAURANT_TAG)
                    FiltersHelper.SPORT_TAG -> this.addChip(FiltersHelper.SPORT_TAG)
                    FiltersHelper.STORE_TAG -> this.addChip(FiltersHelper.STORE_TAG)
                    FiltersHelper.SUPERMARKET_TAG -> this.addChip(FiltersHelper.SUPERMARKET_TAG)
                    FiltersHelper.TRANSPORT_TAG -> this.addChip(FiltersHelper.TRANSPORT_TAG)
                    else -> { }
                }
            }
        }
        else {
            fragment_detail_nearby_container.visibility = View.GONE
        }
    }
    
    // Add new chip in chip group
    private fun addChip(tag: String){
        val chipGroup = fragment_detail_nearby_chip_group
        val chip = Chip(context)
        chip.setChipBackgroundColorResource(R.color.colorPrimary)
        chip.setTextColor(resources.getColor(R.color.colorTextOnPrimary))
        chip.iconStartPadding = 10f
        chip.iconEndPadding = 10f

        when(tag){
            FiltersHelper.SCHOOL_TAG -> {
                chip.text = getString(R.string.chip_school)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_school_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.HEALTH_TAG -> {
                chip.text = getString(R.string.chip_health)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_local_health_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.RESTAURANT_TAG -> {
                chip.text = getString(R.string.chip_restaurant)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_restaurant_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.SPORT_TAG -> {
                chip.text = getString(R.string.chip_sport)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_fitness_center_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.STORE_TAG -> {
                chip.text = getString(R.string.chip_store)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_local_store_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.SUPERMARKET_TAG -> {
                chip.text = getString(R.string.chip_supermarket)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_local_supermarket_24px)
                chipGroup.addView(chip)
            }
            FiltersHelper.TRANSPORT_TAG -> {
                chip.text = getString(R.string.chip_transport)
                chip.chipIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_twotone_transport_24px)
                chipGroup.addView(chip)
            }
        }
    }


    //***************************** MENU + ACTIONS ****************************

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(resources.getBoolean(R.bool.isTabletMode) || resources.getBoolean(R.bool.isTabletLandMode)){
            super.onCreateOptionsMenu(menu, inflater)
        }
        else {
            inflater.inflate(R.menu.conversion_toolbar_menu, fragment_detail_toolbar.menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.conversion_dollars_euros -> this.convertIntoEuros()
            R.id.conversion_euros_dollars -> this.convertIntoDollars()
        }
        return false
    }

    private fun convertIntoDollars(){
        if (!isDollar) {
            this.isDollar = true
            fragment_detail_price.text = Utils.convertEuroToDollar(( this.currentEstate.price.toInt())).toString()
            fragment_detail_currency.text = "$"
        }
    }

    private fun convertIntoEuros(){
        if (isDollar) {
            this.isDollar = false
            fragment_detail_price.text = Utils.convertDollarToEuro(( this.currentEstate.price.toInt())).toString()
            fragment_detail_currency.text = "€"
        }
    }
}
