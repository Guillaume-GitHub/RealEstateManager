package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.view.ItemHomeViewHolder

class ItemHomeAdapter(private val dataset: ArrayList<Estate>, private val callback: RecyclerClickListener.OnEstateClick )
    : RecyclerView.Adapter<ItemHomeViewHolder>() {

    companion object {
        const val EURO_CURRENCY = "€"
        const val DOLLAR_CURRENCY = "$"
    }

    private var currentCurrency = DOLLAR_CURRENCY

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHomeViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        return ItemHomeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemHomeViewHolder, position: Int) {
        holder.bind(dataset[position], callback, currentCurrency)
    }

    override fun getItemCount(): Int {
       return dataset.size
    }

    fun setDollarCurrency(){
        this.currentCurrency = DOLLAR_CURRENCY
    }

    fun setEuroCurrency(){
        this.currentCurrency = EURO_CURRENCY
    }
}