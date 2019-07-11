package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.ItemDraftAdapter
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.activity_draft.*
import kotlinx.android.synthetic.main.fragment_draft.*
import java.util.ArrayList

class DraftFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ItemDraftAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.configRecyclerView()
    }

    //TODO : REMOVE EXAMPLE
    private fun configRecyclerView(){
        recyclerView = fragment_draft_recycler_view.apply {
           this.layoutManager = LinearLayoutManager(context)
            var estates = arrayListOf(Estate("Mansion","Big Mansion front of the sea","653 st Unknow street",199999.0))
            this.adapter = ItemDraftAdapter(estates)

        }
    }
}
