package com.openclassrooms.realestatemanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.RecyclerViewItemClickListener
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.view.ItemHomeViewHolder

class ItemHomeAdapter(private val dataset: ArrayList<Estate>, private val callback: RecyclerViewItemClickListener): RecyclerView.Adapter<ItemHomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHomeViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        return ItemHomeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemHomeViewHolder, position: Int) {
        holder.bind(dataset[position],callback)
    }

    override fun getItemCount(): Int {
        Log.d("RECYCLER ITEM SIZE ", dataset.size.toString() )
       return dataset.size
    }
}