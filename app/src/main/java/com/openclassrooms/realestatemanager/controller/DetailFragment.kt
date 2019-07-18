package com.openclassrooms.realestatemanager.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment(val estate: Estate) : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.bind()
        this.hideActivityToolbar(true)
        this.configureToolbar()
    }

    // Hide activity toolbar and set click back btn
    private fun configureToolbar(){
        this.hideActivityToolbar(true)
        fragment_detail_toolbar.setNavigationOnClickListener(View.OnClickListener {
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

    override fun onDestroyView() {
        this.hideActivityToolbar(false)
        super.onDestroyView()
    }
}
