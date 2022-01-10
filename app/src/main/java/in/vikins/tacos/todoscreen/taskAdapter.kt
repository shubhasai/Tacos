package `in`.vikins.tacos.todoscreen

import `in`.vikins.tacos.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class taskAdapter(private val context:Context, val listener:taskclicked): RecyclerView.Adapter<taskAdapter.taskviewHolder>() {
    val alltask = ArrayList<task>()
    inner class taskviewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var txtview = itemView.findViewById<TextView>(R.id.taskname)
        val delete = itemView.findViewById<ImageView>(R.id.deletetask)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskviewHolder {
        val viewHolder = taskviewHolder(LayoutInflater.from(context).inflate(R.layout.task_single_view,parent,false))
        viewHolder.delete.setOnClickListener {
            listener.ontaskclicked(alltask[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: taskviewHolder, position: Int) {
        val position = alltask[position]
        holder.txtview.text = position.dos

    }

    override fun getItemCount(): Int {
        return alltask.size
    }
    fun updatelist(newlist:List<task>){
        alltask.clear()
        alltask.addAll(newlist)
        notifyDataSetChanged()
    }
}
interface taskclicked{
    fun ontaskclicked(task:task){

    }
}