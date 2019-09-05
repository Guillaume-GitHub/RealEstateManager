package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.EstateCategory
import java.text.FieldPosition

class ItemCategoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_category_item,parent,false)){

    private val imageBackground: ImageView = itemView.findViewById(R.id.recycler_category_item_image_view)
    private val categoryName: TextView = itemView.findViewById(R.id.recycler_category_item_text_view)

    fun displayItem(category: EstateCategory, callback: RecyclerClickListener.OnItemClick, position: Int){
        categoryName.text = category.name
        this.setClickListener(position, callback)
        this.addBackground(category.name)
    }

    private fun setClickListener(position: Int, callback: RecyclerClickListener.OnItemClick) {
        itemView.setOnClickListener {
            callback.onItemClick(position)
        }
    }

    private fun addBackground(category: String){
        when (category){
            "House" -> imageBackground.setBackgroundResource(R.drawable.house)
            "Penthouse"  -> imageBackground.setBackgroundResource(R.drawable.penthouse)
            "Flat" -> imageBackground.setBackgroundResource(R.drawable.flat)
            "Mansion" -> imageBackground.setBackgroundResource(R.drawable.mansion)
            "Duplex" -> imageBackground.setBackgroundResource(R.drawable.duplex)
            else -> {}
        }
    }
}