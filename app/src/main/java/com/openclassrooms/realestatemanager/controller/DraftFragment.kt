package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerViewItemClickListener
import com.openclassrooms.realestatemanager.adapter.ItemDraftAdapter
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_draft.*

class DraftFragment : Fragment(), RecyclerViewItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private val callback: RecyclerViewItemClickListener = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this.configRecyclerView()
    }
/*
    //TODO : REMOVE EXAMPLE
    private fun configRecyclerView(){
        recyclerView = fragment_draft_recycler_view.apply {
           this.layoutManager = LinearLayoutManager(context)
            val estates = arrayListOf(Estate(0,"Mansion","Big Mansion front of the sea","653 st Unknow street",199999,3))
            this.adapter = ItemDraftAdapter(estates, callback)

        }
    }
*/
    // Catch item click to start NewEstateFragment
    override fun onRecyclerViewItemClick(estate: Estate) {
        val newEstateFragment = NewEstateFragment(estate)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.activity_draft_frame_layout, newEstateFragment)
                ?.addToBackStack("newEstateFragment")
                ?.commit()
    }
}
