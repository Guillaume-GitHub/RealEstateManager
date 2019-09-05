package com.openclassrooms.realestatemanager.controller


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.Injections.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.Utils.SwipeToDeleteHelper
import com.openclassrooms.realestatemanager.adapter.ItemDraftAdapter
import com.openclassrooms.realestatemanager.model.entity.Draft
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_draft.*
import kotlin.collections.ArrayList

class DraftFragment : Fragment(), RecyclerClickListener.OnDraftClick, SwipeToDeleteHelper.OnSwipeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemDraftAdapter
    private var draftList = ArrayList<Draft>()
    private val callback:RecyclerClickListener.OnDraftClick = this
    private lateinit var estateViewModel: EstateViewModel
    private lateinit var tempDeletedDraft: Draft
    private  var tempDeletedDraftPosition: Int = -1

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
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHelper(this))
        itemTouchHelper.attachToRecyclerView(this.recyclerView)
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

    override fun onItemSwiped(position: Int) {
        this.tempDeletedDraft = this.adapter.getItem(position)
        this.tempDeletedDraftPosition = position
        this.adapter.deleteItem(position)
        this.notifyItemDeleted()
    }

    private fun notifyItemDeleted() {
        val view = fragment_draft_root_view
        val snackBar = Snackbar.make(view, R.string.draft_snackbar_item_remove_text,Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(resources.getColor(R.color.colorSecondary))
                .setAction(R.string.draft_snackbar_undo_text, View.OnClickListener {
                    this.draftList.add(this.tempDeletedDraftPosition, tempDeletedDraft)
                    this.adapter.notifyDataSetChanged()
        })

        snackBar.addCallback(object: Snackbar.Callback(){
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
               when(event){
                   DISMISS_EVENT_ACTION -> { }
                   else -> deleteDraft(tempDeletedDraft)
               }
            }
        })

        snackBar.show()
    }

    @SuppressLint("CheckResult")
    private fun deleteDraft(draft: Draft){
        this.estateViewModel.deleteDraft(draft).subscribeOn(Schedulers.newThread()).subscribe { _, error ->
            error?.printStackTrace()
        }
    }

}
