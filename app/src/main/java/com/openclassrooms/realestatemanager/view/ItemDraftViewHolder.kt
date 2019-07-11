package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

class ItemDraftViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_draft_item, parent,false)) {

    private val title: TextView = itemView.findViewById(R.id.recyclerview_draft_item_title_textview)
    private val price: TextView = itemView.findViewById(R.id.recyclerview_draft_item_price_textview)
    private val date: TextView = itemView.findViewById(R.id.recyclerview_draft_item_date_textview)

    fun bind(estate: Estate){
        title.text = estate.title
        price.text = estate.price.toString()
        date.text = Calendar.getInstance().time.toString()
    }
}