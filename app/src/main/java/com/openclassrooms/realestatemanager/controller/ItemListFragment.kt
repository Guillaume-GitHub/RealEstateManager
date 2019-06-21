package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.adapter.ItemListAdapter

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_item_list.*

class ItemListFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var estates = ArrayList<Estate>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_item_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.addEstate()
        this.recyclerViewConfig()
    }

    // Configure RecyclerView
    private fun recyclerViewConfig(){
            this.recyclerView = fragment_item_list_recyclerview.apply {
            this.layoutManager = GridLayoutManager(this.context,2)
            this.adapter = ItemListAdapter(estates)
        }
    }

    private fun addEstate(){
        estates.add(Estate("Apartement","Apartment 2 rooms with balcony", "322 Lindsay St Chapel Hill", 130000.00))
        estates.add(Estate("House","Big house, 5 rooms with garden", "112 Brooks St Chapel Hill", 219000.00))
        estates.add(Estate("House","Big house, 4 rooms, 152 m2", "112 Brooks St Chapel Hill", 199999.00))
        estates.add(Estate("Appartement","35 m2, 1 room","152 Lindsay St Chapel Hill", 80000.00 ))
        estates.add(Estate("Business Shop", "Head office of Google France","8 Rue de Londres, 75009 Paris",1000000.00))
    }

}
