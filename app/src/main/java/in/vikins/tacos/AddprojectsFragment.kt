package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentAddprojectsBinding
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AddprojectsFragment : Fragment() {
    val agrs:AddprojectsFragmentArgs by navArgs()
    private lateinit var binding:FragmentAddprojectsBinding
    private lateinit var mfirebasedatabase: DatabaseReference
    private lateinit var mfirebasestorage: StorageReference
    private var imgurl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddprojectsBinding.inflate(layoutInflater)
        mfirebasedatabase = Firebase.database.reference
        val loadimg = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.projectdp.setImageURI(it)
            if (it != null) {
                imgurl = it.toString()
            }
            else{
                val directions = AddprojectsFragmentDirections.actionAddprojectsFragmentToHomeFragment()
                findNavController().navigate(directions)
            }
        }
        binding.projectdp.setOnClickListener {
            loadimg.launch("image/*")
        }
        binding.addproject.setOnClickListener {
            addproject()
        }
        return binding.root
    }
    fun addproject(){
        val user = FirebaseAuth.getInstance().currentUser
        val userid = user?.uid.toString()
        mfirebasestorage = Firebase.storage.reference
        val file: Uri = Uri.parse(imgurl)
        mfirebasestorage.child("projectimage/${binding.ProjectName.text}").putFile(file).addOnSuccessListener{
            binding.addproject.visibility = View.INVISIBLE
            mfirebasestorage.child("projectimage/${binding.ProjectName.text}").downloadUrl.addOnSuccessListener {
                imgurl = it.toString()
            }
            val name = binding.ProjectName.text.toString().lowercase()
            val category = binding.category.text.toString().lowercase()
            val bio = binding.aboutProject.text.toString().lowercase()
            val dp = imgurl
            val database = FirebaseDatabase.getInstance().getReference()
            if(bio.isNotEmpty()||category.isNotEmpty()||name.isNotEmpty()){
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("name",name)
                hashMap.put("author",agrs.author)
                hashMap.put("userid", userid)
                hashMap.put("dp", dp)
                hashMap.put("des", bio)
                hashMap.put("category",category)
                database.child("projects").push().setValue(hashMap)
                Toast.makeText(activity,"Saved", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(activity,"Enter All Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}