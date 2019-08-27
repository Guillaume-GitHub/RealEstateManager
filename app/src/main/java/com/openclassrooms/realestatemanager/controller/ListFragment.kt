package com.openclassrooms.realestatemanager.controller


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
import com.openclassrooms.realestatemanager.adapter.ItemListAdapter
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.entity.Estate
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var estates = ArrayList<Estate>()
    private lateinit var query: SimpleSQLiteQuery


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
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewConfig(){
        this.recyclerView = fragment_list_recyclerview.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = ItemListAdapter(estates)
        }
    }

    // Perform Sql request and notify dataset to show items
    private fun getQueryResult(query: SimpleSQLiteQuery) {
        try {
            AppDatabase.getInstance(context!!)?.rawDao()?.getRawResult(query)?.observe(this, Observer { estatesList ->
                this.estates.addAll(estatesList)
                this.recyclerView.adapter?.notifyDataSetChanged()
            })
        }
        catch (error: NullPointerException) {
            Log.e(this.javaClass.simpleName, error.printStackTrace().toString())
        }
    }
}
