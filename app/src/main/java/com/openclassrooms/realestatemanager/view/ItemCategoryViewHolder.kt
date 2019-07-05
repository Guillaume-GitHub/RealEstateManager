package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.EstateCategory

class ItemCategoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_category_item,parent,false)){

    private val imageBackground: ImageView = itemView.findViewById(R.id.recycler_category_item_image_view)
    private val categoryName: TextView = itemView.findViewById(R.id.recycler_category_item_text_view)

    fun displayItem(category: EstateCategory){
        categoryName.text = category.name
    }
}