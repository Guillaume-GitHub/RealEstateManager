package com.openclassrooms.realestatemanager.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

class BottomSheetImageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.bottom_sheet_recyclerview_item, parent,false)) {

    private var imageView: AppCompatImageView = itemView.findViewById(R.id.recycler_bottom_sheet_image_view)

    fun bind(imageUri: Uri) {
        this.imageView.setImageURI(imageUri)
    }
}