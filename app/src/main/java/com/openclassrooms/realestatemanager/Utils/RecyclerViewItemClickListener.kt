package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.Estate

interface RecyclerViewItemClickListener {
   fun onRecyclerViewItemClick(estate: Estate)
}
