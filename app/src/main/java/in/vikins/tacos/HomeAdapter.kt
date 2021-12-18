package `in`.vikins.tacos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeAdapter(private val context: Context?, private val postlist:ArrayList<postdata>, private val listener: postClicked): RecyclerView.Adapter<HomeAdapter.Homeviewholder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Homeviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_singleview,parent,false)
        val viewHolder = Homeviewholder(view)
        view.setOnClickListener{
            listener.onitemClicked(postlist[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    class Homeviewholder(view: View):RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.ptitle)
        val username:TextView = view.findViewById(R.id.pname)
        val des:TextView = view.findViewById(R.id.ppostdes)
        val imgUser: ImageView = view.findViewById(R.id.puserImage)
        val likebtn:ImageView = view.findViewById(R.id.likeButton)
    }

    override fun onBindViewHolder(holder: Homeviewholder, position: Int) {
        val post = postlist[position]
        holder.username.text = post.author
        holder.title.text = post.title
        holder.des.text = post.des
        Glide.with(context!!).load(post.udp).error(R.drawable.ic_about).into(holder.imgUser)
        Glide.with(context).load(R.drawable.ic_likeb).error(R.drawable.ic_likeb).into(holder.likebtn)
    }

    override fun getItemCount(): Int {
        return postlist.size
    }
}
interface postClicked {
    fun onitemClicked(itemlist: postdata){

    }
}