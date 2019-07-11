package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_main.*

class DetailFragment(val estate: Estate) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.bind()
        configureToolbar()
    }

    // Hide activity toolbar and set click back btn
    private fun configureToolbar(){
        this.hideActivityToolbar(true)
        fragment_detail_toolbar.setNavigationOnClickListener(View.OnClickListener {
            this.hideActivityToolbar(false)
            activity?.onBackPressed()
        })
    }

    private fun hideActivityToolbar(hide: Boolean){
        if (hide)
            (activity as AppCompatActivity).supportActionBar?.hide()
        else
            (activity as AppCompatActivity).supportActionBar?.show()

    }
    private fun bind(){
        fragment_detail_title.text = estate.title
        fragment_detail_price.text = estate.price.toString()
        fragment_detail_address.text = estate.address
    }
}
