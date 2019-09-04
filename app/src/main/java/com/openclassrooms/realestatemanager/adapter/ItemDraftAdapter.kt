package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.view.ItemDraftViewHolder

class ItemDraftAdapter(private var dataset: ArrayList<Draft>, private val callback: RecyclerClickListener.onDraftClick):
        RecyclerView.Adapter<ItemDraftViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDraftViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemDraftViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemDraftViewHolder, position: Int) {
        holder.bind(dataset[position], callback)
    }

    override fun getItemCount(): Int {
       return dataset.size
    }

    fun deleteItem(position: Int) {
        this.dataset.removeAt(position)
    }

    fun getItem(position: Int): Draft {
        return this.dataset[position]
    }
}