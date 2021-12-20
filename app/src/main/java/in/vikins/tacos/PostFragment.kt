package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentPostBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostFragment : Fragment() {
    lateinit var binding:FragmentPostBinding
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(layoutInflater)
        binding.uploadpost.setOnClickListener{
            uploadpost()
        }
        return binding.root
    }

    private fun uploadpost(){
        val title = binding.blogtitle.text.toString()
        val des = binding.blogdes.text.toString()
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userid).get().addOnSuccessListener {
            if(it.exists()){
                val n = it.child("fname").value.toString()
                var name = n
                var d = it.child("dp").value.toString()
                val dp = d
                val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                val hashMap: HashMap<String, Any> = HashMap()
                if(name== ""||title.isEmpty()||des.isEmpty()){
                    Toast.makeText(activity,"Input All Field",Toast.LENGTH_SHORT).show()
                }
                else{
                    hashMap.put("uid",userid)
                    hashMap.put("author", name)
                    hashMap.put("title", title)
                    hashMap.put("des", des)
                    hashMap.put("like",0)
                    hashMap.put("udp",dp)
                    reference.child("post").push().setValue(hashMap)
                    Toast.makeText(activity,"Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    binding.blogdes.setText("")
                    binding.blogtitle.setText("")
                }
            }
        }

    }
}