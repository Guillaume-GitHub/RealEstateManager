package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.OnRecyclerItemClick
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.model.entity.Locality

class ItemHomeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_home_list_item, parent,false)){

    private val title: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_title)
    private val price: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_price)
    private val address: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_location)
    private val image: ImageView = itemView.findViewById(R.id.recyclerview_home_list_item_card_view_image)

    private fun displayItem(estate: Estate){
        title.text = estate.title
        price.text = getPriceText(estate.price)
        address.text = this.getAddressText(estate.locality)

        image.setImageURI(estate.images[0])
    }

    private fun setOnclickListener(callback: OnRecyclerItemClick, estate: Estate){
        this.itemView.setOnClickListener(View.OnClickListener {
            v: View? -> callback.onRecyclerViewItemClick(estate) })
    }

    fun bind(estate: Estate, callback: OnRecyclerItemClick){
        this.displayItem(estate)
        this.setOnclickListener(callback,estate)
    }

    private fun getPriceText(price: Long): String {
        return "$ ${price.toInt()}"
    }

    private fun getAddressText(locality: Locality): String {
       return if(locality.postalCode != null) "${locality.cities} , ${locality.postalCode}" else locality.cities
    }
}