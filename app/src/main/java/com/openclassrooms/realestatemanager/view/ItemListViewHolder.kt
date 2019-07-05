package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate

class ItemListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_list_item, parent,false)){

    private val title: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_title)
    private val price: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_price)
    private val address: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_address)

    fun displayItem(estate: Estate){
        title.text = estate.title
        price.text = estate.price.toInt().toString() + " $"
        address.text = estate.address
    }
}