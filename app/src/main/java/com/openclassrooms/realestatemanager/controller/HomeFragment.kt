package com.openclassrooms.realestatemanager.controller


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.adapter.ItemHomeAdapter

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RecyclerViewItemClickListener
import com.openclassrooms.realestatemanager.adapter.ItemCategoryAdapter
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateCategory
import kotlinx.android.synthetic.main.fragment_main.*

class HomeFragment : Fragment(),RecyclerViewItemClickListener {

    override fun OnRecyclerViewItemclick(position: Int) {
        this.showDetailFragment()
    }

    private lateinit var rootView: View
    private lateinit var itemRecycler: RecyclerView
    private lateinit var categoryRecycler: RecyclerView
    private var estates = ArrayList<Estate>()
    private var categories = ArrayList<EstateCategory>()
    private val RQ_FILTER_ACTIVITY = 5
    private val callback: RecyclerViewItemClickListener = this



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.addEstate()
        this.addCategory()
        this.recyclerViewItemConfig()
        this.recyclerViewCategoryConfig()
        this.onClickLastestEstate()
        this.onClickSearchView()
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewItemConfig(){
            this.itemRecycler = fragment_main_recyclerview_sold.apply {
            this.layoutManager = GridLayoutManager(context,2) as RecyclerView.LayoutManager?
            this.adapter = ItemHomeAdapter(estates,callback)
        }
    }

    // Configure RecyclerView of categories
    private fun recyclerViewCategoryConfig(){
        this.categoryRecycler = fragment_main_recyclerview_categories.apply {
            this.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            this.adapter = ItemCategoryAdapter(categories)
        }
    }

    private fun onClickLastestEstate(){
        fragment_main_latest_estate_imageview.setOnClickListener(View.OnClickListener { v: View? ->
            val detailFragment = DetailFragment()
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    ?.replace(R.id.activity_main_framelayout_list,detailFragment,"detailFragment")
                    ?.addToBackStack(null)
                    ?.commit()
        })
    }

    private fun showDetailFragment(){
            val detailFragment = DetailFragment()
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    ?.replace(R.id.activity_main_framelayout_list,detailFragment,"detailFragment")
                    ?.addToBackStack(null)
                    ?.commit()
    }

    private fun onClickSearchView(){
        fragment_main_search_view.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(context,FilterActivity::class.java)
            startActivityForResult(intent,RQ_FILTER_ACTIVITY)
        })
    }
    //TODO : Remove this example
    private fun addEstate(){
        estates.add(Estate("Apartement","Apartment 2 rooms with balcony", "322 Lindsay St Chapel Hill", 130000.00))
        estates.add(Estate("House","Big house, 5 rooms with garden", "112 Brooks St Chapel Hill", 219000.00))
        estates.add(Estate("House","Big house, 4 rooms, 152 m2", "112 Brooks St Chapel Hill", 199999.00))
        estates.add(Estate("Appartement","35 m2, 1 room","152 Lindsay St Chapel Hill", 80000.00 ))
        estates.add(Estate("Business Shop", "Head office of Google France","8 Rue de Londres, 75009 Paris",1000000.00))
    }

    //TODO : Remove this example
    private fun addCategory(){
        categories.add(EstateCategory("Housse"))
        categories.add(EstateCategory("Apartment"))
        categories.add(EstateCategory("Bungalow"))
        categories.add(EstateCategory("Ranch"))
        categories.add(EstateCategory("Tiny Housse"))
        categories.add(EstateCategory("Colonial"))
        categories.add(EstateCategory("Mansion"))
    }

}
