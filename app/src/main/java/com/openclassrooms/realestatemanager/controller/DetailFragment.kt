package com.openclassrooms.realestatemanager.controller

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.ItemImageAdapter
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment(private var viewModel: EstateViewModel, private var estateId: Long) : Fragment(){

    private var imagesList = ArrayList<Uri>()
    private lateinit var adapter: ItemImageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerViewItemConfig()
        this.getEstate(estateId)
    }

    private fun bind(estate: Estate){
        fragment_detail_title.text = estate.title
        fragment_detail_price.text = estate.price.toString()
        fragment_detail_date.text = estate.publishedDate.toString()
        fragment_detail_address.text = estate.address
        fragment_detail_description.text = estate.description
        fragment_detail_seller_name.text = estate.agent.name + " ${estate.agent.surname}"
        this.addImagesToRecycler(estate.images)
    }

    private fun getEstate(id: Long){
        this.viewModel.getEstate(id)!!.observe(this, Observer { estate ->

            if(estate != null) { this.bind(estate) }
            else { Log.w(this::class.java.simpleName, "Fail to get Estate")}
        })
    }

    private fun addImagesToRecycler(images: ArrayList<Uri>){
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
}
