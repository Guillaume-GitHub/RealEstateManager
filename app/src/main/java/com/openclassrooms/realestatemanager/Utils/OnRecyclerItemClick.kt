package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.Estate

interface OnRecyclerItemClick {
   fun onRecyclerViewItemClick(estate: Estate)
}
