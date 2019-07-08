package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RecyclerViewItemClickListener
import com.openclassrooms.realestatemanager.model.Estate

class ItemHomeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_home_list_item, parent,false)){

    private val title: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_title)
    private val price: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_price)
    private val address: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_location)

    fun displayItem(estate: Estate){
        title.text = estate.title
        price.text = estate.price.toInt().toString() + " $"
        address.text = estate.address
    }

    fun setOnclickListener(callback:RecyclerViewItemClickListener, position: Int){
        this.itemView.setOnClickListener(View.OnClickListener {
            v: View? -> callback.OnRecyclerViewItemclick(position) })
    }
}