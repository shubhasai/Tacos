package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class groupsAdapter(private val context: Context?, private val grplist:ArrayList<grpdata>, private val listener: grpClicked):RecyclerView.Adapter<groupsAdapter.GrpViewHolder>() {
    class GrpViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val grpname: TextView = view.findViewById(R.id.grpn)
        val no: TextView = view.findViewById(R.id.matesreq)
        val des: TextView = view.findViewById(R.id.aboutgrp)
        val gdp: ImageView = view.findViewById(R.id.grpdp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grp_single_view,parent,false)
        val viewHolder = GrpViewHolder(view)
        view.setOnClickListener{
            listener.ongrpClicked(grplist[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder:GrpViewHolder, position: Int) {
        val grp = grplist[position]
        holder.grpname.text = grp.name
        holder.des.text = "More Detaits: "+ grp.teambio
        holder.no.text = "Member Limit: "+(grp.strength)
        Glide.with(context!!).load(grp.grpdp).error(R.drawable.ic_about).into(holder.gdp)
    }

    override fun getItemCount(): Int {
        return grplist.size
    }
}

interface grpClicked {
    fun ongrpClicked(itemlist: grpdata){

    }
}
