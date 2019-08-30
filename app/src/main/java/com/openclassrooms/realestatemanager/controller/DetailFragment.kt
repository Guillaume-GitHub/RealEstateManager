package com.openclassrooms.realestatemanager.controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.Injections.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.FiltersHelper
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(){

    private var imagesList = ArrayList<Uri>()
    private lateinit var adapter: ItemImageAdapter
    private lateinit var recyclerView: RecyclerView
    private var  estateId: Long = -1
    private lateinit var viewModel: EstateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle: Bundle = this.arguments!!
        this.estateId = bundle.getLong("estate_id")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerViewItemConfig()
        this.getEstate(estateId)
        this.configureFloatingEditBtn()
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

    private fun bind(estate: Estate){
        // Images section
        this.addImagesToRecycler(estate.images)
        // Title section
        fragment_detail_title.text = estate.title
        fragment_detail_price.text = estate.price.toString()
        fragment_detail_date.text = estate.publishedDate.toString()
        // Criteria section
        fragment_detail_type_text.text = estate.category
        fragment_detail_surface_text.text =  this.getSurfaceText(estate)
        fragment_detail_room_text.text = this.getRoomText(estate)
        // Description section
        fragment_detail_description.text = estate.description
        fragment_detail_address.text = estate.locality.formattedAddress
        // Nearby section
        this.showNearbyPOI(estate.filters)
        // Posted By section
        fragment_detail_agent_name.text = this.getAgentText(estate)
    }

    private fun getEstate(id: Long) {
        val activity = activity as DetailActivity
        if (activity.getViewModel() != null) {
            this.viewModel = activity.getViewModel()!!
        }
        else {
            val viewModelFactory = Injection.provideViewModelFactory(context!!)
            this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
        }

        this.fetchEstate(id)
    }

    private fun fetchEstate(id: Long){
        this.viewModel.getEstate(id)!!.observe(this, Observer { estate ->
            if(estate != null) { this.bind(estate) }
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
}
