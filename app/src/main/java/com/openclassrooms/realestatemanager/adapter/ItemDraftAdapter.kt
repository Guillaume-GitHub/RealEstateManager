package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.view.ItemDraftViewHolder

class ItemDraftAdapter(var dataset: ArrayList<Estate>): RecyclerView.Adapter<ItemDraftViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDraftViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemDraftViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemDraftViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
       return dataset.size
    }
}