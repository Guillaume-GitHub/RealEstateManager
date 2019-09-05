package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.model.entity.Locality
import java.util.*

class ItemHomeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_home_list_item, parent,false)){

    private val title: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_title)
    private val price: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_price)
    private val address: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_location)
    private val image: ImageView = itemView.findViewById(R.id.recyclerview_home_list_item_card_view_image)
    private val imageIndicator: Chip = itemView.findViewById(R.id.recyclerview_home_list_item_nb_photo)
    private val badge: AppCompatTextView = itemView.findViewById(R.id.recyclerview_home_list_item_card_view_sold)

    fun bind(estate: Estate, callback: RecyclerClickListener.onEstateClick ){
        this.displayItem(estate)
        this.setOnclickListener(callback,estate)
    }

    private fun displayItem(estate: Estate){
        this.title.text = estate.title
        this.price.text = getPriceText(estate.price)
        this.address.text = this.getAddressText(estate.locality)
        this.image.setImageURI(estate.images[0])
        this.imageIndicator.text = "${estate.images.count()}"
        this.showStatus(estate.saleDate)
    }

    private fun setOnclickListener(callback: RecyclerClickListener.onEstateClick, estate: Estate){
        this.itemView.setOnClickListener{
            callback.onEsateItemClick(estate)
        }
    }

    private fun getPriceText(price: Long): String {
        return "$ ${price.toInt()}"
    }

    private fun getAddressText(locality: Locality): String {
       return if(locality.postalCode != null) "${locality.cities} , ${locality.postalCode}" else locality.cities
    }

    private fun showStatus(saleDate: Date?) {
        if(saleDate != null) this.badge.visibility = View.VISIBLE
        else this.badge.visibility = View.GONE
    }
}