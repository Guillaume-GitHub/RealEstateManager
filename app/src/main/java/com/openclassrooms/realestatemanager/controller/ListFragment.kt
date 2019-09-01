package com.openclassrooms.realestatemanager.controller


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.QueryBuilder
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.adapter.ItemListAdapter
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.entity.Estate
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_main.*

class ListFragment : Fragment(), RecyclerClickListener.onEstateClick {

    companion object {
        private const val REQUEST_FILTER_RESULT = 10
    }

    private lateinit var recyclerView: RecyclerView
    private var estates = ArrayList<Estate>()
    private lateinit var query: SimpleSQLiteQuery
    private val callback: RecyclerClickListener.onEstateClick  = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            val bundle = arguments
            this.query = SimpleSQLiteQuery(bundle?.getString("query"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerViewConfig()
        this.getQueryResult(this.query)
        this.onClickSearchView()
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewConfig(){
        this.recyclerView = fragment_list_recyclerview.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = ItemListAdapter(estates, callback)
        }
    }

    // Start FilterActivity when search bar clicked
    private fun onClickSearchView(){
        fragment_list_search_view.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(context,FilterActivity::class.java)
            startActivityForResult(intent, REQUEST_FILTER_RESULT)
        })
    }

    // Perform Sql request and notify dataset to show items
    private fun getQueryResult(query: SimpleSQLiteQuery) {
        try {
            AppDatabase.getInstance(context!!)?.rawDao()?.getRawResult(query)?.observe(this, Observer { estatesList ->
                this.estates.clear()
                this.estates.addAll(estatesList)
                this.recyclerView.adapter?.notifyDataSetChanged()
            })
        }
        catch (error: NullPointerException) { error.printStackTrace() }
    }

    override fun onEsateItemClick(estate: Estate) {
        startActivity(Intent(context, DetailActivity::class.java).putExtra("ESTATE_ID",estate.estateUid))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_FILTER_RESULT ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT OK")
                        if (data != null) {
                            getQueryResult(SimpleSQLiteQuery(QueryBuilder().getQuery(data)))
                        }
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT CANCELED")
                    }
                }
        }
    }
}
