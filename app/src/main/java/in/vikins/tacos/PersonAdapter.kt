package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class PersonAdapter(private val context: Context?, private val userlist:ArrayList<profiledata>, val listener:userClicked):RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.rn)
        val txtskill:TextView = view.findViewById(R.id.rwork)
        val imgUser: CircleImageView = view.findViewById(R.id.rdp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_singleview,parent,false)
        val viewHolder =  ViewHolder(view)
        view.setOnClickListener{
            listener.onuserClicked(userlist[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userlist[position]
        holder.txtUserName.text = user.fname
        holder.txtskill.text = user.work
        Glide.with(context!!).load(user.dp).error(R.drawable.ic_about).into(holder.imgUser)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }
}
interface userClicked{
    fun onuserClicked(itemlist: profiledata){

    }
}