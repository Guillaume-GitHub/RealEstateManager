package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.model.Estate

interface RecyclerViewItemClickListener {
   fun onRecyclerViewItemclick(estate: Estate)
}
