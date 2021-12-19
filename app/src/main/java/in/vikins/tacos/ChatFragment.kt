package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentChatBinding
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment(),userClicked {
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user?.uid.toString()
    lateinit var binding:FragmentChatBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)
        binding.userlists.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        val reqdatabase = FirebaseDatabase.getInstance().getReference("users")
        val personArray:ArrayList<profiledata> = ArrayList()
        reqdatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                personArray.clear()
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val ruser = datasnapshot.getValue(profiledata::class.java)
                    if (ruser != null && (ruser.uid != userid)) {
                        personArray.add(ruser)
                    }
                }
                val personadapter = PersonAdapter(activity as Context?, personArray,this@ChatFragment)
                binding.userlists.adapter = personadapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return binding.root
    }

    override fun onuserClicked(itemlist: profiledata) {
        val direction = ChatFragmentDirections.actionChatFragmentToMsgFragment(itemlist.uid,itemlist.fname)
        findNavController().navigate(direction)
    }
}