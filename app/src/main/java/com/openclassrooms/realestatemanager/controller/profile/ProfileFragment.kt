package com.openclassrooms.realestatemanager.controller.profile


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.adapter.ItemListAdapter
import com.openclassrooms.realestatemanager.controller.detail.DetailActivity
import com.openclassrooms.realestatemanager.controller.estate.NewEstateActivity
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), RecyclerClickListener.OnEstateClick, View.OnClickListener {

    companion object {
        const val ARG_UID = "uid"
        const val FRAGMENT_TAG = "profile_frag_tag"
    }

    private lateinit var editProfileFrag: EditProfileFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemListAdapter
    private lateinit var estateList: ArrayList<Estate>
    private lateinit var currentAgent: Agent
    private lateinit var viewModel: EstateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.configureViewModel()
        this.getAgentFromArgs(arguments)
        this.configRecyclerView()
        fragment_profile_profile_container.setOnClickListener(this)
        fragment_profile_add_btn.setOnClickListener(this)
    }

    //************************* CONFIG *************************

    private fun configRecyclerView(){
        this.recyclerView = fragment_profile_recycler_view
        this.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        this.estateList = ArrayList()
        this.adapter = ItemListAdapter(this.estateList,this)
        this.recyclerView.adapter = this.adapter
        this.showHideRecycler()
    }

    private fun configureViewModel() {
        val activity: ProfileActivity = activity as ProfileActivity
        this.viewModel = activity.viewModel
    }

    //************************* UI / FRAGMENT **********************

    // Show EditProfileFragment
    private fun showEditProfileFragment(){
        val frag = activity?.supportFragmentManager?.findFragmentByTag(EditProfileFragment.FRAGMENT_TAG)
        if (frag != null)
            this.editProfileFrag = frag as EditProfileFragment
        else
            this.editProfileFrag = EditProfileFragment()

        val args = Bundle()
        args.putLong(EditProfileFragment.ARG_UID, this.currentAgent.uid)

        this.editProfileFrag.arguments = args

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.activity_profile_frame_layout, this.editProfileFrag, FRAGMENT_TAG)
                ?.addToBackStack(null)
                ?.commit()
    }

    // SHOW or HIDE recycler view if null
    private fun showHideRecycler(){
        if(this.estateList.isNullOrEmpty()){
            fragment_profile_recycler_view.visibility = View.GONE
            fragment_profile_empty_list_view.visibility = View.VISIBLE
        }
        else {
            fragment_profile_recycler_view.visibility = View.VISIBLE
            fragment_profile_empty_list_view.visibility = View.GONE
        }
    }

    private fun bindAgent(agent: Agent?){
        if (agent != null){
            fragment_profile_name.text = "${agent.name} ${agent.surname}"
            fragment_profile_image.apply {
                if (agent.image != null) setImageURI(agent.image)
                else setBackgroundResource(R.drawable.ic_account_circle_black_24dp)
            }
            // Fetch his posted estates
            this.getEstates()
        }
    }

    //************************* MENU *************************

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }
        return true
    }

    //************************* CALLBACK CLICK *************************

    override fun onEstateItemClick(estate: Estate) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("ESTATE_ID", estate.estateUid)
        this.startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v){
            fragment_profile_profile_container -> this.showEditProfileFragment()
            fragment_profile_add_btn -> startActivity(Intent(context, NewEstateActivity::class.java))
            else -> { }
        }
    }

    //************************* DATABASE *************************

    // Get Estates posted by this agent
    private fun getEstates(){
        this.viewModel.getEstatesPostedBy(this.currentAgent.uid)?.observe(this, Observer { estatesList ->
            this.estateList.clear()
            this.estateList.addAll(estatesList)
            this.adapter.notifyDataSetChanged()
            this.showHideRecycler()
        })
    }

    // Get Agent From database
    private fun fetchAgent(id: Long){
        if (id != -1L) {
            this.viewModel.getAgent(id)?.observe(this, Observer { agent ->
                this.currentAgent = agent
                this.bindAgent(this.currentAgent)
            })
        }
    }

    //*********************************************************

    // Get arguments from bundle (to create Agent)
    private fun getAgentFromArgs(args: Bundle?){
       if (args != null) {
           val uid = args.getLong(ARG_UID, -1L)
           this.fetchAgent(uid)
       }
    }

}
