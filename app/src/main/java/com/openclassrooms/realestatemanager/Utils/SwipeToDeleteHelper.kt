package com.openclassrooms.realestatemanager.Utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteHelper(private val callback: OnSwipeListener): ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        this.callback.onItemSwiped(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    interface OnSwipeListener{
        fun onItemSwiped(position: Int)
    }
}