package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentHomeBinding
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(),postClicked {
    lateinit var postArray:ArrayList<postdata>
    lateinit var layoutManager: RecyclerView.LayoutManager
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid.toString()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.child(userid).get().addOnSuccessListener {
            if(it.exists()){
                val name = it.child("fname").value.toString()
                binding.uName.setText(name)
            }
        }
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.postlist.layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(activity)
        postArray = ArrayList()
        getpostlist()
        return binding.root
    }

    fun getpostlist(){
        val database = FirebaseDatabase.getInstance().getReference("post")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postArray.clear()
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val post = datasnapshot.getValue(postdata::class.java)
                    postArray.add(post!!)
                }
                val postAdapter = HomeAdapter(activity as Context?, postArray,this@HomeFragment)
                binding.postlist.adapter =postAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onitemClicked(itemlist: postdata) {
//        val dbase = FirebaseDatabase.getInstance().getReference("posts")
//        val query = dbase.orderByChild("des").equalTo(itemlist.des)
//        query.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for ( ds:DataSnapshot in snapshot.children){
//                    val updates: MutableMap<String, Any> = HashMap()
//                    updates.put("like",itemlist.like+1)
//                    ds.ref.updateChildren(updates)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }
}