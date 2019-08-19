package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerViewItemClickListener
import com.openclassrooms.realestatemanager.model.Estate

class ItemHomeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_home_list_item, parent,false)){

    private val title: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_title)
    private val price: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_price)
    private val address: TextView =  itemView.findViewById(R.id.recyclerview_home_list_item_text_container_location)
    private val image: ImageView = itemView.findViewById(R.id.recyclerview_home_list_item_card_view_image)

    private fun displayItem(estate: Estate){
        title.text = estate.title
        price.text = estate.price.toInt().toString() + " $"
        address.text = estate.locality.cities + ", ${estate.locality.postalCode}"
        image.setImageURI(estate.images[0])
    }

    private fun setOnclickListener(callback: RecyclerViewItemClickListener, estate: Estate){
        this.itemView.setOnClickListener(View.OnClickListener {
            v: View? -> callback.onRecyclerViewItemClick(estate) })
    }

    fun bind(estate: Estate, callback: RecyclerViewItemClickListener){
        this.displayItem(estate)
        this.setOnclickListener(callback,estate)
    }
}