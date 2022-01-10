package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class myprojectAdapter(private val context: Context?, private val projectlist:ArrayList<projectData>):RecyclerView.Adapter<myprojectAdapter.myprojectviewHolder>() {
    class myprojectviewHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.findViewById(R.id.projecttitle)
        val projectpic:ImageView = view.findViewById(R.id.projectpic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myprojectviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.about_myproject_singleview,parent,false)
        val viewHolder = myprojectviewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: myprojectviewHolder, position: Int) {
        val project = projectlist[position]
        holder.name.text = project.name
        Glide.with(context!!).load(project.dp).error(R.drawable.ic_project).into(holder.projectpic)
    }

    override fun getItemCount(): Int {
       return projectlist.size
    }
}