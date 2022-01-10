package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentChatBinding
import `in`.vikins.tacos.databinding.FragmentGrpchatBinding
import `in`.vikins.tacos.databinding.FragmentMsgBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GrpchatFragment : Fragment() {
    private lateinit var binding:FragmentGrpchatBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    val cuser = FirebaseAuth.getInstance().currentUser
    val cuserid = cuser?.uid.toString()
    var reference: DatabaseReference? = null
    private var chatList = ArrayList<grpchatdata>()
    val args:GrpchatFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGrpchatBinding.inflate(layoutInflater)
        binding.grpchatRecyclerView.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        binding.grpbtnSendMessage.setOnClickListener {
            sendMessage(cuserid,binding.grpetMessage.text.toString(),args.personname)
        }
        readMessage()
        return binding.root
    }
    private fun sendMessage(senderId: String, message: String,name:String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("sendername", name)
        hashMap.put("userid", senderId)
        hashMap.put("message", message)
        reference!!.child("Groupchat").child(args.grpname).push().setValue(hashMap)
        binding.grpetMessage.setText(" ")

    }
    fun readMessage() {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Groupchat").child(args.grpname)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(grpchatdata::class.java)

                    if (chat != null) {
                        chatList.add(chat)
                    }
                }
                val grpchatAdapter = GroupchatAdapter(chatList)
                binding.grpchatRecyclerView.adapter = grpchatAdapter
            }
        })
    }
}