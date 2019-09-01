package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.model.entity.Estate

abstract class RecyclerClickListener: OnRecyclerEstateClick{

    interface onDraftClick {
        fun onDraftItemClick(draft: Draft) {}
    }

    interface onEstateClick {
        fun onEsateItemClick(estate: Estate) {}
    }
}