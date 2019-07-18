package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.EstateImage

class ItemImageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_image_item, parent,false)) {

    private var imageView: AppCompatImageView = itemView.findViewById(R.id.recyclerview_image_item_image_view)

    fun bind(image: EstateImage){
        if (image.imageBitmap != null){
            this.imageView.setImageBitmap(image.imageBitmap)
        }
        else if(image.imageUri != null){
            this.imageView.setImageURI(image.imageUri)
        }

    }
}