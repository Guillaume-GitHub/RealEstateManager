package com.openclassrooms.realestatemanager.controller


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {

    private lateinit var viewModel: EstateViewModel
    private lateinit var currentAgent: Agent
    private var agentId: Long = -1
    private var isModification = false

    companion object{
        const val ARG_UID = "uid"
        const val FRAGMENT_TAG = "edit_profile_frag_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.configureViewModel()
        this.getArgs(arguments)
    }

    //************************* MENU *****************************

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.valid_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> activity?.onBackPressed()
            R.id.toolbar_valid_btn -> this.insertAgent()
        }
        return true
    }

    //************************* DATABASE *************************

    // Insert New agent
    private fun insertAgent(){
        val agent = Agent(0, fragment_profile_first_name_text.text.toString(), fragment_profile_last_name_text.text.toString(), null)
        this.viewModel.insertAgent(agent)
    }

    // Get Agent From database
    private fun fetchAgent(id: Long){
        if (agentId != -1L) {
            this.viewModel.getAgent(id)?.observe(this, Observer { agent ->
                this.currentAgent = agent
                this.bind(this.currentAgent)
            })
        }
    }

    //Get view Model
    private fun configureViewModel() {
        val activity: ProfileActivity = activity as ProfileActivity
        this.viewModel = activity.viewModel
    }

    //************************* *************************

    // BIND view if agent was fetched
    private fun bind(agent: Agent) {
        this.isModification = true
        fragment_profile_first_name_text.setText("${agent.name}")
        fragment_profile_last_name_text.setText("${agent.surname}")
        fragment_edit_profile_image.apply {
            if (agent.image != null) setImageURI(agent.image)
            else setImageResource(R.drawable.ic_account_circle_black_24dp)
        }
    }

    private fun getArgs(arguments: Bundle?){
        if (arguments != null){
            this.agentId = arguments.getLong(ARG_UID, -1)
            this.fetchAgent(this.agentId)
        }
    }


}
