package com.openclassrooms.realestatemanager.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.view.ItemImageViewHolder

class ItemImageAdapter(var dataset: ArrayList<Uri>): RecyclerView.Adapter<ItemImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemImageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}