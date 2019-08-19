package com.openclassrooms.realestatemanager.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

class ItemImageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_image_item, parent,false)) {

    private var imageView: AppCompatImageView = itemView.findViewById(R.id.recyclerview_image_item_image_view)

    fun bind(imageUri: Uri) {
        this.imageView.setImageURI(imageUri)
    }

}