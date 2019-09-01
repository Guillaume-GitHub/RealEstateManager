package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Injections.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.adapter.ItemDraftAdapter
import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_draft.*
import java.util.*
import kotlin.collections.ArrayList

class DraftFragment : Fragment(), RecyclerClickListener.onDraftClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemDraftAdapter
    private var draftList = ArrayList<Draft>()
    private val callback:RecyclerClickListener.onDraftClick = this
    private lateinit var estateViewModel: EstateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.configureViewModel()
        this.configRecyclerView()
        this.fetchDraft()
    }

    private fun configRecyclerView(){
        this.recyclerView = fragment_draft_recycler_view
        this.recyclerView.layoutManager = LinearLayoutManager(context)
        this.adapter = ItemDraftAdapter(draftList, callback)
        this.recyclerView.adapter = this.adapter
    }

    override fun onDraftItemClick(draft: Draft) {
        val newEstateFragment = NewEstateFragment()
        val bundle = Bundle()
        bundle.putLong("draft_id", draft.draftUid)
        newEstateFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.activity_draft_frame_layout, newEstateFragment, NewEstateActivity.FRAGMENT_TAG)
                ?.addToBackStack(null)
                ?.commit()
    }

    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory(context!!)
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
    }

    private fun fetchDraft(){
        this.estateViewModel.getAllDraft()?.observe(this,androidx.lifecycle.Observer { list ->
            this.draftList.clear()
            this.draftList.addAll(list)
            this.adapter.notifyDataSetChanged()
        })
    }
}
