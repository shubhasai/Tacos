package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentAboutBinding
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AboutFragment : Fragment() {
    var imgurl:String = ""
    var skills:String = ""
    private lateinit var binding: FragmentAboutBinding
    private lateinit var mfirebasedatabase: DatabaseReference
    private lateinit var mfirebasestorage: StorageReference
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
        readdata()
        binding = FragmentAboutBinding.inflate(layoutInflater)
        mfirebasedatabase = Firebase.database.reference
        val loadimg = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.AboutUdp.setImageURI(it)
            if (it != null) {
                imgurl = it.toString()
            }
            else{
                val directions = AboutFragmentDirections.actionAboutFragmentToHomeFragment()
                findNavController().navigate(directions)
            }
        }
        binding.AboutUdp.setOnClickListener {
            loadimg.launch("image/*")
        }
        // Inflate the layout for this fragment
        binding.save.setOnClickListener {
            binding.save.visibility = View.VISIBLE
            if(imgurl ==""){
                Toast.makeText(activity,"Select Image",Toast.LENGTH_SHORT).show()
                loadimg.launch("image/*")
            }
            else{
                savedata()
            }
        }
        return binding.root
    }
    private fun savedata(){

        mfirebasestorage = Firebase.storage.reference
        val file: Uri = Uri.parse(imgurl)
        mfirebasestorage.child("image/$userid").putFile(file).addOnSuccessListener{
            binding.save.visibility = View.INVISIBLE
            mfirebasestorage.child("image/$userid").downloadUrl.addOnSuccessListener {
                mfirebasedatabase.child("users").child(userid).child("dp").setValue(it.toString())
                imgurl = it.toString()
            }
            val bio = binding.bio.text.toString().lowercase()
            skills = binding.work.text.toString()
            val resumelink = binding.resume.text.toString()
            val dp = imgurl
            if(bio.isNotEmpty()||skills.isNotEmpty()||resumelink.isNotEmpty()){
                mfirebasedatabase.child("users").child(userid).child("bio").setValue(bio)
                mfirebasedatabase.child("users").child(userid).child("work").setValue(skills)
                mfirebasedatabase.child("users").child(userid).child("resume_link").setValue(resumelink)
                mfirebasedatabase.child("users").child(userid).child("dp").setValue(dp)
                Toast.makeText(activity,"Saved",Toast.LENGTH_SHORT).show()
                readdata()
                val direction= AboutFragmentDirections.actionAboutFragmentToHomeFragment()
                findNavController().navigate(direction)
            }
            else{
                Toast.makeText(activity,"Enter All Fields",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun readdata(){
        mfirebasestorage = Firebase.storage.reference
        mfirebasestorage.child("image/${userid}").downloadUrl.addOnSuccessListener {
            mfirebasedatabase.child("users").child(userid).child("dp").setValue(it.toString())
        }
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userid).get().addOnSuccessListener {
            if(it.exists()){
                val p = it.child("dp").value
                Glide.with(this).load(p).error(R.drawable.ic_about).into(binding.AboutUdp)
                val n = it.child("fname").value.toString().uppercase()
                binding.AboutUName.setText(n)
                val w = it.child("work").value.toString()
                binding.work.setText(w)
                val b = it.child("bio").value.toString()
                binding.bio.setText(b)
                val r = it.child("resume_link").value.toString()
                binding.resume.setText(r)
            }
        }
    }
}