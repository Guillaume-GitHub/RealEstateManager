package com.openclassrooms.realestatemanager.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.appyvet.materialrangebar.RangeBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity(), RangeBar.OnRangeBarChangeListener {

    // Range bar change listener
    override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
        if(rangeBar != null){

            when(rangeBar){
                activity_filter_price_rangebar -> {
                    activity_filter_price_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if((rightPinIndex * rangeBar.tickInterval).toInt() >= 1000000){
                        activity_filter_price_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    }
                    else{
                        activity_filter_price_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }

                activity_filter_surface_rangebar -> {
                    activity_filter_surface_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if ((rightPinIndex * rangeBar.tickInterval).toInt() >= 1000) {
                        activity_filter_surface_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    } else {
                        activity_filter_surface_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }

                activity_filter_room_rangebar -> {
                    activity_filter_room_min_text.text = (leftPinIndex * rangeBar.tickInterval).toInt().toString()
                    if ((rightPinIndex * rangeBar.tickInterval).toInt() >= 1000) {
                        activity_filter_room_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString() + "+"
                    } else {
                        activity_filter_room_max_text.text = (rightPinIndex * rangeBar.tickInterval).toInt().toString()
                    }
                }
            }
        }
    }

    override fun onTouchStarted(rangeBar: RangeBar?) {
    }

    override fun onTouchEnded(rangeBar: RangeBar?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        this.configToolbar()

        this.setUpRangeBar(activity_filter_price_rangebar,0,1000000,25000, this)
        this.setUpRangeBar(activity_filter_surface_rangebar,0,1000,50,this)
        this.setUpRangeBar(activity_filter_room_rangebar, 0,20,1,this)
    }

    // Set up toolbar
    private fun configToolbar() {
        // Set the toolbar as support action bar
        setSupportActionBar(activity_filter_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 5.0f
    }

    // Set up rangebar
    private fun setUpRangeBar(rangeBar: RangeBar,minVal: Int, maxVal: Int, interval: Int, listener: RangeBar.OnRangeBarChangeListener){
        rangeBar.tickStart = minVal.toFloat()
        rangeBar.tickEnd = maxVal.toFloat()
        rangeBar.setTickInterval(interval.toFloat())
        rangeBar.setOnRangeBarChangeListener(listener)

        when(rangeBar){
            activity_filter_price_rangebar -> {
                activity_filter_price_min_text.text = minVal.toString()
                activity_filter_price_max_text.text = maxVal.toString() + " +"
            }

            activity_filter_surface_rangebar -> {
                activity_filter_surface_min_text.text = minVal.toString()
                activity_filter_surface_max_text.text = maxVal.toString() + " +"
            }

            activity_filter_room_rangebar -> {
                activity_filter_room_min_text.text = minVal.toString()
                activity_filter_room_max_text.text = maxVal.toString() + " +"
            }
        }
    }

}
