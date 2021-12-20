package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GroupdetailsmAdapter(private val context: Context?, private val reqlist:ArrayList<profiledata>):RecyclerView.Adapter<GroupdetailsmAdapter.GrpmViewHolder>() {
    class GrpmViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val rname: TextView = view.findViewById(R.id.rn)
        val rwork: TextView = view.findViewById(R.id.rwork)
        val rdp: ImageView = view.findViewById(R.id.rdp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrpmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_singleview,parent,false)
        val viewHolder = GrpmViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: GrpmViewHolder, position: Int) {
        val req = reqlist[position]
        holder.rname .text = req.fname
        holder.rwork.text = req.work
        Glide.with(context!!).load(req.dp).error(R.drawable.ic_about).into(holder.rdp)
    }

    override fun getItemCount(): Int {
        return reqlist.size
    }
}