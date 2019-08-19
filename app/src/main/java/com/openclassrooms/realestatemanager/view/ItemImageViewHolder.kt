package com.openclassrooms.realestatemanager.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.OnDeleteImageButtonClick

class ItemImageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_image_item, parent,false)) {

    private var imageView: AppCompatImageView = itemView.findViewById(R.id.recyclerview_image_item_image_view)
    private var deleteBtn: AppCompatImageButton = itemView.findViewById(R.id.recyclerview_image_item_delete_button)

    fun bind(imageUri: Uri) {
        this.deleteBtn.visibility = View.INVISIBLE
        this.imageView.setImageURI(imageUri)
    }

    fun addDeleteBtnClickListener(deleteBtnCallback: OnDeleteImageButtonClick?, position: Int){
        this.deleteBtn.visibility = View.VISIBLE
        deleteBtn.setOnClickListener {
            deleteBtnCallback?.onDeleteButtonClick(position)
        }
    }

}