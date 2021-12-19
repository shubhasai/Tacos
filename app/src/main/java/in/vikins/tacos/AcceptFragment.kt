package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentAboutBinding
import `in`.vikins.tacos.databinding.FragmentAcceptBinding
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AcceptFragment : Fragment() {
    private val args:AcceptFragmentArgs by navArgs()
    private lateinit var mfirebasedatabase: DatabaseReference
    private lateinit var mfirebasestorage: StorageReference
    lateinit var binding:FragmentAcceptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mfirebasedatabase = Firebase.database.reference
        binding = FragmentAcceptBinding.inflate(layoutInflater)
        mfirebasestorage = Firebase.storage.reference
        mfirebasestorage.child("image/${args.uid}").downloadUrl.addOnSuccessListener {
            mfirebasedatabase.child("users").child(args.uid).child("dp").setValue(it.toString())
        }
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(args.uid).get().addOnSuccessListener {
            if(it.exists()){
                val p = it.child("dp").value
                Glide.with(this).load(p).error(R.drawable.ic_about).into(binding.reqUdp)
                val n = it.child("fname").value.toString().uppercase()
                binding.reqUName.setText(n)
                val w = it.child("work").value.toString()
                binding.reqwork.setText(w)
                val b = it.child("bio").value.toString()
                binding.reqbio.setText(b)
                val r = it.child("resume_link").value.toString()
                binding.reqresume.setText(r)
            }
        }
        binding.accent.setOnClickListener {
            val memberlist: ArrayList<String> = ArrayList()
            val database = FirebaseDatabase.getInstance().getReference("groups")
            database.child(args.grpname).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (datasnapshot: DataSnapshot in snapshot.children) {
                        val grp = datasnapshot.getValue(grpdata::class.java)
                        if (grp != null && grp.name == args.grpname) {
                            for (member in grp.request) {
                                memberlist.add(member)
                            }
                        }
                    }
                    memberlist.add(args.uid)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            database.child(args.grpname).child("members").push().setValue(args.uid)
        }
        return binding.root
    }
}