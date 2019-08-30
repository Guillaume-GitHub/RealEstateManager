package com.openclassrooms.realestatemanager.controller

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.adapter.ItemHomeAdapter

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.NotificationHelper
import com.openclassrooms.realestatemanager.Utils.OnRecyclerItemClick
import com.openclassrooms.realestatemanager.Utils.QueryBuilder
import com.openclassrooms.realestatemanager.adapter.ItemCategoryAdapter
import com.openclassrooms.realestatemanager.model.EstateCategory
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class HomeFragment : Fragment(), OnRecyclerItemClick {

    private lateinit var rootView: View
    private lateinit var itemRecycler: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var estateAdapter: ItemHomeAdapter
    private lateinit var categoryRecycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var categoryAdapter: ItemCategoryAdapter
    private lateinit var  viewModel: EstateViewModel

    //private var viewModel = estateViewModel
    private var estates = ArrayList<Estate>()
    private var categories = ArrayList<EstateCategory>()
    private val RQ_FILTER_ACTIVITY = 5
    private val callback: OnRecyclerItemClick = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false)
        Log.d("HomeFrag $this ", "CREATE VIEW")
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initViewModel()
        this.recyclerViewItemConfig()
        this.addEstate()

        this.recyclerViewCategoryConfig()
        this.addCategory()

        this.onClickLatestEstate()
        this.onClickFilterBtn()
        this.onClickFloatingAddButton()
    }

    // Get the View Model
    private fun initViewModel(){
        val activity = activity as MainActivity
        if (activity.getViewModel() != null) {
            this.viewModel = activity.getViewModel()!!
        }
        else {
            val viewModelFactory = Injection.provideViewModelFactory(context!!)
            this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
        }
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewItemConfig(){
        this.itemRecycler = fragment_main_recyclerview_sold
        this.estateAdapter = ItemHomeAdapter(estates,callback)
        this.gridLayoutManager = GridLayoutManager(context,2)
        this.itemRecycler.layoutManager = this.gridLayoutManager
        this.itemRecycler.adapter = this.estateAdapter
    }

    // Configure RecyclerView of categories
    private fun recyclerViewCategoryConfig(){
        this.categoryRecycler = fragment_main_recyclerview_categories
        this.linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        this.categoryAdapter = ItemCategoryAdapter(categories)
        this.categoryRecycler.layoutManager = this.linearLayoutManager
        this.categoryRecycler.adapter = this.categoryAdapter
        LinearSnapHelper().attachToRecyclerView(this.categoryRecycler)
    }

    // Display ListFragment when "Latest Estate" ImageView clicked
    private fun onClickLatestEstate() {
        fragment_main_latest_estate_imageview.setOnClickListener(View.OnClickListener { v: View? ->
            val listFragment = ListFragment()
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    ?.replace(R.id.activity_main_framelayout_list,listFragment)
                    ?.addToBackStack("listFragment")
                    ?.commit()
        })
    }

    private fun showDetailFragment(estate: Estate){
       startActivity(Intent(context, DetailActivity::class.java).putExtra("ESTATE_ID",estate.estateUid))
    }

    // Start FilterActivity when search bar clicked
    private fun onClickFilterBtn(){
        fragment_main_search_view.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(context,FilterActivity::class.java)
            startActivityForResult(intent,RQ_FILTER_ACTIVITY)
        })
    }

    // Start NewEstateActivity when floating btn clicked
    private fun onClickFloatingAddButton(){
        activity_main_floating_btn.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(context, NewEstateActivity::class.java))
        })
    }

    override fun onRecyclerViewItemClick(estate: Estate) {
       this.showDetailFragment(estate)
        for (i in 0 until estate.images.size) { Log.d("URI ", estate.images[i].path) }
    }


    private fun addEstate(){
        this.getAllEstate()
    }

    private fun getAllEstate(){
        this.viewModel.getLatestEstate()?.observe(this, Observer { list->
            estates.clear()
            estates.addAll(list)
            estateAdapter.notifyDataSetChanged()
        })
    }

    private fun addCategory(){
        val cat = resources.getStringArray(R.array.categoryArray)
        var i = 0
        cat.forEach { str ->
            categories.add(EstateCategory(i.toLong(),str))
            i++
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RQ_FILTER_ACTIVITY ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT OK")
                        if (data != null) {

                            val arg = Bundle()
                            arg.putString("query", QueryBuilder().getQuery(data))
                            val frag = ListFragment()
                            frag.arguments = arg

                            activity?.supportFragmentManager?.beginTransaction()
                                    ?.replace(R.id.activity_main_framelayout_list, frag)
                                    ?.addToBackStack("showQueryResult")
                                    ?.commit()
                        }
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT CANCELED")
                    }
                }
        }
    }

    override fun onDestroy() {
        Log.d("HomeFrag $this ", "DESTROY")
        super.onDestroy()
    }
}
