package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.view.ItemListViewHolder

class ItemListAdapter(private val dataset: ArrayList<Estate>): RecyclerView.Adapter<ItemListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemListViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.displayItem(dataset[position])
    }

    override fun getItemCount(): Int {
        return  dataset.size
    }
}