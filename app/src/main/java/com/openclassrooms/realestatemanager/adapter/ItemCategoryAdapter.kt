package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.model.EstateCategory
import com.openclassrooms.realestatemanager.view.ItemCategoryViewHolder

class ItemCategoryAdapter(private var dataset: List<EstateCategory>) : RecyclerView.Adapter<ItemCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemCategoryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.displayItem(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}