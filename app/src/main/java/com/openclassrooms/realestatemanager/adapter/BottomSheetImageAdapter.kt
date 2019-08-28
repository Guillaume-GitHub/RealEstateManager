package com.openclassrooms.realestatemanager.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.view.BottomSheetImageViewHolder

class BottomSheetImageAdapter(private var images: ArrayList<Uri>): RecyclerView.Adapter<BottomSheetImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BottomSheetImageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: BottomSheetImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun getItem(position: Int): Uri{
        return this.images[position]
    }

    override fun getItemCount(): Int {
        return this.images.size
    }
}