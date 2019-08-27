package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Utils.OnRecyclerItemClick
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.view.ItemHomeViewHolder

class ItemHomeAdapter(private val dataset: ArrayList<Estate>, private val callback: OnRecyclerItemClick): RecyclerView.Adapter<ItemHomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHomeViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        return ItemHomeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemHomeViewHolder, position: Int) {
        holder.bind(dataset[position],callback)
    }

    override fun getItemCount(): Int {
       return dataset.size
    }
}