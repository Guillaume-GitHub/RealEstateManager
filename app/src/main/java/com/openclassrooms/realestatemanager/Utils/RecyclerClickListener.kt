package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.model.entity.Estate

abstract class RecyclerClickListener{


    interface OnDraftClick {
        fun onDraftItemClick(draft: Draft) {}
    }

    interface OnEstateClick {
        fun onEstateItemClick(estate: Estate) {}
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}