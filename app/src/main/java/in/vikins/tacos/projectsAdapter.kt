package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class projectsAdapter (private val context: Context?, private val projectslist:ArrayList<projectData>): RecyclerView.Adapter<projectsAdapter.projectsViewHolder>() {
    class projectsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pname:TextView = view.findViewById(R.id.ProjectsName)
        val aut:TextView = view.findViewById(R.id.author)
        val category:TextView = view.findViewById(R.id.categories)
        val des:TextView = view.findViewById(R.id.aboutProjects)
        val pdp:ImageView = view.findViewById(R.id.projectspic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): projectsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_singleview,parent,false)
        val viewHolder = projectsViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: projectsViewHolder, position: Int) {
        val projects = projectslist[position]
        holder.pname.text = projects.name
        holder.des.text = projects.des
        holder.category.text = projects.category
        holder.aut.text = projects.author
        Glide.with(context!!).load(projects.dp).error(R.drawable.ic_projectimage).into(holder.pdp)
    }

    override fun getItemCount(): Int {
        return projectslist.size
    }
}