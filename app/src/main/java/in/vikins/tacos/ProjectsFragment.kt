package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentProjectsBinding
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProjectsFragment : Fragment() {
    private lateinit var binding:FragmentProjectsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProjectsBinding.inflate(layoutInflater)
        binding.projectslist.layoutManager = LinearLayoutManager(context)
        val reqdatabase = FirebaseDatabase.getInstance().getReference("projects")
        val projectArray:ArrayList<projectData> = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser
        val userid = user?.uid.toString()
        reqdatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                projectArray.clear()
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val ruser = datasnapshot.getValue(projectData::class.java)
                    if (ruser != null) {
                        projectArray.add(ruser)
                    }
                }
                val personadapter = projectsAdapter(activity as Context?, projectArray)
                binding.projectslist.adapter = personadapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return binding.root
    }

}