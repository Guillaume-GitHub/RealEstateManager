package com.openclassrooms.realestatemanager.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Utils.OnDeleteImageButtonClick
import com.openclassrooms.realestatemanager.view.ItemImageViewHolder

class ItemImageAdapter(private var dataset: ArrayList<Uri>, private var deleteBtnCallback: OnDeleteImageButtonClick?)
    : RecyclerView.Adapter<ItemImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemImageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(dataset[position])
        if (this.deleteBtnCallback != null) holder.addDeleteBtnClickListener(deleteBtnCallback,position)
        holder.setNbImagesIndicator(this.dataset.size)
    }

    fun getItem(position: Int): Uri{
        return this.dataset[position]
    }

    override fun getItemCount(): Int {
        return this.dataset.size
    }
}