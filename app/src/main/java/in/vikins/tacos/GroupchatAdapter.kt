package `in`.vikins.tacos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class GroupchatAdapter(private val grpchatList: ArrayList<grpchatdata>): RecyclerView.Adapter<GroupchatAdapter.GrpChatViewHolder>() {
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user?.uid.toString()
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    class GrpChatViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtUsermsg: TextView = view.findViewById(R.id.grptvMessage)
        val pername: TextView = view.findViewById(R.id.pername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrpChatViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.grp_chat_rightside, parent, false)
            return GrpChatViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.grp_chat_leftside, parent, false)
            return GrpChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: GrpChatViewHolder, position: Int) {
        val chat = grpchatList[position]
        holder.pername.text = chat.sendername
        holder.txtUsermsg.text = chat.message
    }

    override fun getItemCount(): Int {
        return grpchatList.size
    }
    override fun getItemViewType(position: Int): Int {
        if (grpchatList[position].userid == userid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }
}