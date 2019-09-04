package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import kotlinx.android.synthetic.main.fragment_simulation.*


class SimulationFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simulation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_simulation_loan_details_section.visibility = View.GONE

        fragment_simulation_submit_btn.setOnClickListener {
            this.estimate()
        }
    }

    private fun checkForm() {
        if (fragment_simulation_loan_amount_text.text.isNullOrBlank()) fragment_simulation_loan_amount_text.error = getString(R.string.simulation_error_empty_value)
        else fragment_simulation_loan_amount_text.error = null

        when {
            fragment_simulation_loan_rate_text.text.isNullOrBlank() -> fragment_simulation_loan_rate_text.error = getString(R.string.simulation_error_empty_value)
            fragment_simulation_loan_rate_text.text.toString().toDouble() > 30.0 -> fragment_simulation_loan_rate_text.error = getString(R.string.simulation_error_high_rate)
            else -> fragment_simulation_loan_rate_text.error = null
        }

        when {
            fragment_simulation_loan_duration_text.text.isNullOrBlank() -> fragment_simulation_loan_duration_text.error = getString(R.string.simulation_error_empty_value)
            fragment_simulation_loan_duration_text.text.toString().toInt() > 50 -> fragment_simulation_loan_duration_text.error = getString(R.string.simulation_error_long_duration)
            else -> fragment_simulation_loan_duration_text.error = null
        }
    }


    private fun isFormValid(): Boolean {
        this.checkForm()
        return fragment_simulation_loan_amount_text.error.isNullOrBlank() &&
                fragment_simulation_loan_rate_text.error.isNullOrBlank() &&
                fragment_simulation_loan_duration_text.error.isNullOrBlank()
    }

    private fun estimate() {

        if (isFormValid()) {
            var amountContribution = 0

            if (!fragment_simulation_loan_contribution_text.text.isNullOrBlank() &&
                    fragment_simulation_loan_contribution_text.text.toString().toInt() < fragment_simulation_loan_amount_text.text.toString().toInt()) {
               amountContribution = fragment_simulation_loan_contribution_text.text.toString().toInt()
            }

            val amountBorrowed = fragment_simulation_loan_amount_text.text.toString().toInt().minus(amountContribution)
            val loanRate = fragment_simulation_loan_rate_text.text.toString().toDouble()
            val duration = (fragment_simulation_loan_duration_text.text.toString().toInt() * 12)

            fragment_simulation_loan_details_section.visibility = View.VISIBLE
            fragment_simulation_result.text = "$ ${Utils.getMonthlyPayment(amountBorrowed, loanRate, duration)}"
            fragment_simulation_amount_borrowed.text = "$ $amountBorrowed"
            fragment_simulation_cost.text = " $ ${(amountBorrowed * (loanRate / 10)).toInt()}"
        }
    }
}
