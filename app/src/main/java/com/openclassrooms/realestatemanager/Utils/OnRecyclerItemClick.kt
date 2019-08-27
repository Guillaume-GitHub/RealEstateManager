package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.entity.Estate

interface OnRecyclerItemClick {
   fun onRecyclerViewItemClick(estate: Estate)
}
