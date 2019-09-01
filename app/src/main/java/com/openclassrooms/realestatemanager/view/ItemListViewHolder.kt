package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.entity.Estate

class ItemListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_list_item, parent,false)) {

    private var image: AppCompatImageView = itemView.findViewById(R.id.recycler_list_item_image)
    private var title: AppCompatTextView = itemView.findViewById(R.id.recycler_list_item_title)
    private var category: AppCompatTextView = itemView.findViewById(R.id.recycler_list_item_category)
    private var location: AppCompatTextView = itemView.findViewById(R.id.recycler_list_item_location)
    private var price: AppCompatTextView = itemView.findViewById(R.id.recycler_list_item_price)
    private var date: AppCompatTextView = itemView.findViewById(R.id.recycler_list_item_date)

    fun displayItem(estate: Estate , callback: RecyclerClickListener.onEstateClick){
        this.image.setImageURI(estate.images[0])
        this.title.text = estate.title
        this.category.text = estate.category
        this.location.text = "${estate.locality.cities}, ${estate.locality.postalCode}"
        this.price.text = estate.price.toString()
        this.date.text = estate.publishedDate.toString()

        this.setOnclickListener(callback, estate)
    }

    private fun setOnclickListener(callback: RecyclerClickListener.onEstateClick, estate: Estate){
        this.itemView.setOnClickListener(View.OnClickListener {
            callback.onEsateItemClick(estate)
        })
    }
}