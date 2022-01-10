package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentGroupdetailsBinding
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
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
    val userid = user?.uid.toString()
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var mlayoutManager: RecyclerView.LayoutManager
    lateinit var binding:FragmentGroupdetailsBinding
    lateinit var grpname: String
    private var reqlist:ArrayList<String> = ArrayList()
    private var memlist:ArrayList<String> = ArrayList()
    val args:GroupdetailsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGroupdetailsBinding.inflate(layoutInflater)
        binding.requestlist.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        binding.memberslist.layoutManager = LinearLayoutManager(activity)
        mlayoutManager = LinearLayoutManager(activity)
        grpname = args.grpname
        getgroup()
        loadmembers()
        loadreq()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        getgroup()
//        loadmembers()
//        loadreq()
        binding.grpmsg.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("users")

            database.child(userid).get().addOnSuccessListener {

                if(it.exists()){
                    val n = it.child("fname").value.toString().uppercase()
                    val direction = GroupdetailsFragmentDirections.actionGroupdetailsFragmentToGrpchatFragment(args.grpname,n)
                    findNavController().navigate(direction)
                }
            }

        }
        return binding.root
        
    }
    fun loadreq(){
        Toast.makeText(activity,args.grpname,Toast.LENGTH_SHORT).show()
        val database = FirebaseDatabase.getInstance().getReference("groups")
        database.child(args.grpname).child("requests").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val req = datasnapshot.getValue(memberdata::class.java)
                    if (req != null) {
                        reqlist.add(req.mid)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val reqdatabase = FirebaseDatabase.getInstance().getReference("users")
        val personArray:ArrayList<profiledata> = ArrayList()
        reqdatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                personArray.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val ruser = datasnapshot.getValue(profiledata::class.java)
                    if (ruser != null) {
                        for (req in reqlist){
                            if (req == ruser.uid){
                                personArray.add(ruser)
                            }
                        }
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
    override fun onreqClicked(itemlist: profiledata) {
        Log.d("uid",itemlist.uid)
        val direction = GroupdetailsFragmentDirections.actionGroupdetailsFragmentToAcceptFragment(itemlist.uid,args.grpname)
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
                        if(grp.name == args.grpname){
                            binding.gn.setText(grp.name)
                            activity?.let { Glide.with(it).load(grp.grpdp).error(R.drawable.ic_about).into(binding.gpic) }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun loadmembers(){

        val database = FirebaseDatabase.getInstance().getReference("groups")
        database.child(args.grpname).child("members").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val req = datasnapshot.getValue(memberdata::class.java)
                    if (req != null) {
                        memlist.add(req.mid)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val reqdatabase = FirebaseDatabase.getInstance().getReference("users")
        val personArray:ArrayList<profiledata> = ArrayList()
        personArray.clear()
        reqdatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                personArray.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val ruser = datasnapshot.getValue(profiledata::class.java)
                    if (ruser != null) {
                        for (req in memlist){
                            if (req == ruser.uid){
                                personArray.add(ruser)
                            }
                        }
                    }
                }
                val reqAdapter = GroupdetailsmAdapter(activity as Context?, personArray)
                binding.memberslist.adapter = reqAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}