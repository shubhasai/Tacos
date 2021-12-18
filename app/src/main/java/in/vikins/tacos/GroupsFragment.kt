package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentCreategrpBinding
import `in`.vikins.tacos.databinding.FragmentGroupsBinding
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


class GroupsFragment : Fragment(),grpClicked {
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid
    var grpname = ""
    lateinit var grpArray:ArrayList<grpdata>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var binding: FragmentGroupsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupsBinding.inflate(layoutInflater)

        binding.needateam.setOnClickListener {
            val direction = GroupsFragmentDirections.actionGroupsFragmentToCreategrpFragment()
            findNavController().navigate(direction)
        }
        // recyclerview
        binding.teamsList.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        grpArray = ArrayList()
        getgrpslist()
        binding.toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.Myt -> {
                        filter()
                    }
                    R.id.Joint -> {
                        getgrpslist()
                    }
                }
            } else {
                if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                    getgrpslist()
                }
            }

        }
        return binding.root
    }
    fun getgrpslist(){
        val database = FirebaseDatabase.getInstance().getReference("groups")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                grpArray.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val grp = datasnapshot.getValue(grpdata::class.java)
                    if (grp != null) {
                        grpname = grp.name
                        for (member in grp.members)
                        {
                            if (member != userid){
                                grpArray.add(grp)
                            }
                        }
                    }
                }
                val postAdapter = groupsAdapter(activity as Context?, grpArray,this@GroupsFragment)
                binding.teamsList.adapter =postAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun filter(){
        val database = FirebaseDatabase.getInstance().getReference("groups")
        val grpArray:ArrayList<grpdata> = ArrayList()
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                grpArray.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val grp = datasnapshot.getValue(grpdata::class.java)
                    if (grp != null) {
                        for (member in grp.members)
                        {
                            if (member == userid){
                                grpArray.add(grp)
                            }
                        }
                    }
                }
                val personAdapter = groupsAdapter(activity as Context?, grpArray,this@GroupsFragment)
                binding.teamsList.adapter = personAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun ongrpClicked(itemlist: grpdata) {
       handle()
    }
    fun handle(){
        val cdatabase = FirebaseDatabase.getInstance().getReference("groups")
        cdatabase.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val grp = datasnapshot.getValue(grpdata::class.java)
                    if (grp != null) {
                        for (member in grp.members) {
                            if (member == userid) {
                                val direction = GroupsFragmentDirections.actionGroupsFragmentToGroupdetailsFragment(grpname)
                                findNavController().navigate(direction)
                            }
                            else{
                                val reqlist: ArrayList<String> = ArrayList()
                                val database = FirebaseDatabase.getInstance().getReference("groups")
                                database.child(grpname).addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (datasnapshot: DataSnapshot in snapshot.children) {
                                            val grp = datasnapshot.getValue(grpdata::class.java)
                                            if (grp != null && grp.name == grpname) {
                                                for (member in grp.request) {
                                                    reqlist.add(member)
                                                }
                                            }
                                        }
                                        if (userid != null) {
                                            reqlist.add(userid)
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                                database.child(grpname).child("request").setValue(reqlist)
                                Toast.makeText(activity,"Membership Requested",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
