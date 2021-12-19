package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GroupdetailsAdapter(private val context: Context?, private val reqlist:ArrayList<profiledata>, private val listener: reqClicked):RecyclerView.Adapter<GroupdetailsAdapter.GrpdViewHolder>() {
    class GrpdViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val rname: TextView = view.findViewById(R.id.grpn)
        val rwork: TextView = view.findViewById(R.id.aboutgrp)
        val rdp: ImageView = view.findViewById(R.id.grpdp)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): GrpdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_singleview,parent,false)
        val viewHolder = GrpdViewHolder(view)
        view.setOnClickListener{
            listener.onreqClicked(reqlist[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: GrpdViewHolder, position: Int) {
        val req = reqlist[position]
        holder.rname .text = req.fname
        holder.rwork.text = req.work
        Glide.with(context!!).load(req.dp).error(R.drawable.ic_about).into(holder.rdp)
    }

    override fun getItemCount(): Int {
        return reqlist.size
    }
}
interface reqClicked {
    fun onreqClicked(itemlist: profiledata){

    }
}