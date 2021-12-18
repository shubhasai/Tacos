package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentCreategrpBinding
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CreategrpFragment : Fragment() {
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid.toString()
    lateinit var binding:FragmentCreategrpBinding
    private lateinit var mfirebasedatabase:DatabaseReference
    var imgurl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreategrpBinding.inflate(layoutInflater)
        val loadimg = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.grpImage.setImageURI(it)
            if (it != null) {
                imgurl = it.toString()
            }
            else{
                val directions = AboutFragmentDirections.actionAboutFragmentToHomeFragment()
                findNavController().navigate(directions)
            }
        }
        binding.grpImage.setOnClickListener {
            loadimg.launch("image/*")
        }
        binding.creategrp.setOnClickListener {
            binding.creategrp.visibility = View.VISIBLE
            if(imgurl ==""){
                Toast.makeText(activity,"Select Image", Toast.LENGTH_SHORT).show()
                loadimg.launch("image/*")
            }
            else{
                Creategrp()
            }
        }
        return binding.root
    }
    fun Creategrp(){
        val n = binding.grpname.text.toString()
        val mfirebasestorage = Firebase.storage.reference
        val file: Uri = Uri.parse(imgurl)
        mfirebasestorage.child("grpimage/$userid/n").putFile(file).addOnSuccessListener{
            binding.creategrp.visibility = View.INVISIBLE
            mfirebasestorage.child("grpimage/$userid/n").downloadUrl.addOnSuccessListener {
                mfirebasedatabase.child(n).child("grpdp").setValue(it.toString())
                imgurl = it.toString()
            }

            val dp = imgurl
            val mateno = binding.matesno.text.toString()
            val grpdes = binding.grpdes.text.toString()
            val memberlist: ArrayList<String> = ArrayList()
            val requestlist: ArrayList<String> = ArrayList()
            mfirebasedatabase = Firebase.database.reference.child("groups")
            memberlist.add(userid)
            val grpProfile = grpdata(n,mateno,grpdes,dp,memberlist,requestlist)

            if(n.isNotEmpty()||mateno.isNotEmpty()||grpdes.isNotEmpty()||dp.isNotEmpty()){
                mfirebasedatabase.child(n).setValue(grpProfile)
            }
            else{
                Toast.makeText(activity,"Enter All Fields",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
