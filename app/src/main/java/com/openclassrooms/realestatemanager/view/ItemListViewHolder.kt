package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate

class ItemListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_list_item, parent,false)) {

    var image = itemView.findViewById<ImageView>(R.id.recycler_list_item_image)
    var title = itemView.findViewById<TextView>(R.id.recycler_list_item_title)
    var category = itemView.findViewById<TextView>(R.id.recycler_list_item_category)
    var location = itemView.findViewById<TextView>(R.id.recycler_list_item_location)
    var price = itemView.findViewById<TextView>(R.id.recycler_list_item_price)
    var date = itemView.findViewById<TextView>(R.id.recycler_list_item_category)

    fun displayItem(estate: Estate){
        //TODO : displayItem() method body
    }
}