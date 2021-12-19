package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentGroupdetailsBinding
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupdetailsFragment : Fragment(),reqClicked {
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid
    var grpname = ""
    lateinit var reqArray:ArrayList<profiledata>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var binding:FragmentGroupdetailsBinding
    val args:GroupdetailsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        grpname = args.grpname
        binding = FragmentGroupdetailsBinding.inflate(layoutInflater)
        binding.requestlist.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        reqArray = ArrayList()
        getgroup()
        loadreq()
        return binding.root
        
    }
    fun loadreq(){
        val database = FirebaseDatabase.getInstance().getReference("groups")
        database.child(grpname).child("requests").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val req = datasnapshot.getValue(memberdata::class.java)

                    if (req != null) {
//                        if (req.mid != userid){
//
//                        }
                        val reqdatabase = FirebaseDatabase.getInstance().getReference("users")
                        val personArray:ArrayList<profiledata> = ArrayList()
                        reqdatabase.addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                personArray.clear()
                                for (datasnapshot:DataSnapshot in snapshot.children){
                                    val user = datasnapshot.getValue(profiledata::class.java)
                                    if(user!!.uid == req.mid ){
                                        personArray.add(user)
                                    }
                                }
                                val reqAdapter = GroupdetailsAdapter(activity as Context?, personArray,this@GroupdetailsFragment)
                                binding.requestlist.adapter = reqAdapter
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    override fun onreqClicked(itemlist: profiledata) {
        val direction = GroupdetailsFragmentDirections.actionGroupdetailsFragmentToAcceptFragment(itemlist.uid,grpname)
        findNavController().navigate(direction)
    }
    fun getgroup(){
        var grpArray:ArrayList<grpdata> = ArrayList()
        val gdatabase = FirebaseDatabase.getInstance().getReference("groups")
        gdatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                grpArray.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val grp = datasnapshot.getValue(grpdata::class.java)
                    if (grp != null) {
                        binding.gn.setText(grp.name)
                        Glide.with(context!!).load(grp.grpdp).error(R.drawable.ic_about).into(binding.gpic)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}