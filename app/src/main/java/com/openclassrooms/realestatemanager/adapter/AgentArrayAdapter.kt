package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.openclassrooms.realestatemanager.R

import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.view.UserDropdownViewHolder

class AgentArrayAdapter(context: Context, @LayoutRes var layoutRes:Int, private var agentArray: ArrayList<Agent>)
    : ArrayAdapter<Agent>(context,layoutRes, agentArray){

    private lateinit var view: View
    private lateinit var holder: UserDropdownViewHolder

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (convertView == null) {

            view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_user, parent, false)

            holder = UserDropdownViewHolder()
            holder.userNameText = view.findViewById(R.id.spinner_dropdown_user_text)
            //holder.userImage = view.findViewById(R.id.spinner_dropdown_user_image)

            view.tag = holder
        }
        else {
            view = convertView
            holder = convertView.tag as UserDropdownViewHolder
        }

        this.bind(view, getItem(position))

        return view
    }

    override fun getItem(position: Int): Agent {
        return this.agentArray[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.agentArray.size
    }

    private fun bind(row: View, agent: Agent) {
        //val userImage = row.findViewById(R.id.spinner_dropdown_user_image) as ImageView
        val userText = row.findViewById(R.id.spinner_dropdown_user_text) as TextView
        userText.text = agent.name + " ${agent.surname}"
        /*
        if (agent.image == null){
            userImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_twotone_account_circle_24px))
        }
        */
    }
}