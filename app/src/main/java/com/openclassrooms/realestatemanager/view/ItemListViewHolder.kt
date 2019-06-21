package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate

class ItemListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_list_item, parent,false)){

    val title: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_title)
    val category: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_category)
    val price: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_price)
    val address: TextView =  itemView.findViewById(R.id.fragment_item_list_item_text_container_address)



    fun displayItem(estate: Estate){
        title.text = estate.title
        category.text = estate.category
        price.text = estate.price.toString()
        address.text = estate.address
    }
}