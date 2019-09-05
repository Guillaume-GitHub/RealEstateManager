package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.EstateCategory
import com.openclassrooms.realestatemanager.view.ItemCategoryViewHolder

class ItemCategoryAdapter(private var dataset: List<EstateCategory>,
                          private val callback: RecyclerClickListener.OnItemClick) : RecyclerView.Adapter<ItemCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemCategoryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.displayItem(dataset[position], callback, position)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int): EstateCategory {
        return this.dataset[position]
    }
}