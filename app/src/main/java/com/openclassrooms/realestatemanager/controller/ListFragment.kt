package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.ItemListAdapter
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    private var estates = ArrayList<Estate>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEstate()
        recyclerViewConfig()
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewConfig(){
        this.recyclerView = fragment_list_recyclerview.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = ItemListAdapter(estates)
        }
    }

    //TODO : Remove this example
    private fun addEstate(){
        estates.add(Estate("Apartement","Apartment 2 rooms with balcony", "322 Lindsay St Chapel Hill", 130000.00))
        estates.add(Estate("House","Big house, 5 rooms with garden", "112 Brooks St Chapel Hill", 219000.00))
        estates.add(Estate("House","Big house, 4 rooms, 152 m2", "112 Brooks St Chapel Hill", 199999.00))
        estates.add(Estate("Appartement","35 m2, 1 room","152 Lindsay St Chapel Hill", 80000.00 ))
        estates.add(Estate("Business Shop", "Head office of Google France","8 Rue de Londres, 75009 Paris",1000000.00))
    }
}
