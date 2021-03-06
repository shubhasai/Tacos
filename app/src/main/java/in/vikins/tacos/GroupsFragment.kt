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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates


class GroupsFragment : Fragment(),grpClicked {
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid.toString()
    var grpname = ""
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
        getgrptest()
//        binding.mygrps.setOnClickListener{
//            getmygrptest()
//        }
        return binding.root
    }
    override fun ongrpClicked(itemlist: grpdata) {
        val cdatabase = FirebaseDatabase.getInstance().getReference("groups")
        val q = cdatabase.child(itemlist.name).child("members").orderByChild("mid").equalTo(userid)
        q.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val memcheck:ArrayList<memberdata> = ArrayList()
                for(ds:DataSnapshot in snapshot.children){
                    val data = ds.getValue(memberdata::class.java)
                    if (data != null) {
                        memcheck.add(data)
                    }
                }
                if (memcheck.size != 0){
                    val direction = GroupsFragmentDirections.actionGroupsFragmentToGroupdetailsFragment(itemlist.name)
                    view?.findNavController()?.navigate(direction)
                }
                else{
                    val rmemarray:ArrayList<memberdata> = ArrayList()
                    val crdatabase = FirebaseDatabase.getInstance().getReference("groups")
                    val r = crdatabase.child(itemlist.name).child("requests").orderByChild("mid").equalTo(userid)
                    r.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var rcheck: Boolean
                            for(rds:DataSnapshot in snapshot.children){
                                val rmem = rds.getValue(memberdata::class.java)
                                if (rmem != null) {
                                    rmemarray.add(rmem)
                                }
                            }
                            rcheck = rmemarray.size == 0
                            if (rcheck){
                                val rdatabase = FirebaseDatabase.getInstance().getReference("groups")
                                val hashMap: HashMap<String, String> = HashMap()
                                hashMap.put("mid",userid)
                                rdatabase.child(itemlist.name).child("requests").push().setValue(hashMap)
                                Toast.makeText(activity,"Membership Requested",Toast.LENGTH_SHORT).show()

                            }
                            else{
                                Toast.makeText(activity,"Membership Already Requested",Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
//        cdatabase.child(itemlist.name).child("members").addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                for (ds:DataSnapshot in snapshot.children){
//                    val members = ds.getValue(memberdata::class.java)
//                    if (members != null) {
//                        if (members.mid == userid.toString()){
//
//                            val direction = GroupsFragmentDirections.actionGroupsFragmentToGroupdetailsFragment(itemlist.name)
//                            view?.findNavController()?.navigate(direction)
//                            break
//                        }
//                        else{
//

//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }
    fun getgrptest(){
        val database = FirebaseDatabase.getInstance().getReference("groups")
        val grpArray:ArrayList<grpdata> = ArrayList()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                grpArray.clear()
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val grp= datasnapshot.getValue(grpdata::class.java)
                    if (grp != null) {
                        grpArray.add(grp)
                    }
                }
                val postAdapter = groupsAdapter(activity as Context?, grpArray,this@GroupsFragment)
                binding.teamsList.adapter =postAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
        })
    }
//    fun getmygrptest(){
//        val database = FirebaseDatabase.getInstance().getReference("groups")
//        val grpArray:ArrayList<grpdata> = ArrayList()
////        database.addValueEventListener(object : ValueEventListener {
////            override fun onDataChange(snapshot: DataSnapshot) {
////                grpArray.clear()
////                for (datasnapshot: DataSnapshot in snapshot.children){
////                    val grp= datasnapshot.getValue(grpdata::class.java)
////                    val db = FirebaseDatabase.getInstance().getReference("groups")
////                    db.child(grp!!.name).child("members").addListenerForSingleValueEvent(object:ValueEventListener{
////                        override fun onDataChange(snapshot: DataSnapshot) {
////                            for(data:DataSnapshot in snapshot.children){
////                                val mem = data.getValue(memberdata::class.java)
////                                if (mem!!.mid == userid){
////                                    grpArray.add(grp)
////                                }
////                            }
////                        }
////                        override fun onCancelled(error: DatabaseError) {
////                            TODO("Not yet implemented")
////                        }
////
////                    })
////                }
////                val postAdapter = groupsAdapter(activity as Context?, grpArray,this@GroupsFragment)
////                binding.teamsList.adapter =postAdapter
////            }
////            override fun onCancelled(error: DatabaseError) {
////                Toast.makeText(activity,"Something Went Wrong",Toast.LENGTH_SHORT).show()
////            }
////        })
//    }
}