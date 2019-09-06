package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.model.entity.Estate

interface OnRecyclerEstateClick {
    fun onEstateItemClick(estate: Estate)
    fun onDraftItemClick(draft: Draft)
    fun onItemClick(position: Int)
}
