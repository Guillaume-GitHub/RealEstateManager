package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.model.entity.Draft

class ItemDraftViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_draft_item, parent,false)) {

    private val title: AppCompatTextView = itemView.findViewById(R.id.recyclerview_draft_item_title_textview)
    private val price: AppCompatTextView = itemView.findViewById(R.id.recyclerview_draft_item_price_textview)
    private val date: AppCompatTextView = itemView.findViewById(R.id.recyclerview_draft_item_date_textview)
    private val image: AppCompatImageView = itemView.findViewById(R.id.recyclerview_draft_item_image)


    fun bind(mDraft: Draft, callback: RecyclerClickListener.OnDraftClick){

        this.title.text = mDraft.title

        val priceText = mDraft.price?.toInt().toString()
        if (mDraft.price != null ) this.price.text = "$ $priceText"

        this.date.text = "${this.date.text} ${mDraft.lastModification}"

        this.image.apply {
            val img = mDraft.images
            if(!img.isNullOrEmpty()) setImageURI(img[0])
        }

        this.setClickListener(mDraft, callback)
    }

    private fun setClickListener(draft: Draft, callback: RecyclerClickListener.OnDraftClick){
        itemView.setOnClickListener {
           callback.onDraftItemClick(draft)
        }
    }
}