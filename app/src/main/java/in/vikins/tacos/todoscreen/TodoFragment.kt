package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentTodoBinding
import `in`.vikins.tacos.todoscreen.task
import `in`.vikins.tacos.todoscreen.taskAdapter
import `in`.vikins.tacos.todoscreen.taskclicked
import `in`.vikins.tacos.todoscreen.todoViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class TodoFragment : Fragment(),taskclicked {
    private lateinit var binding: FragmentTodoBinding
    lateinit var viewModel: todoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(todoViewModel::class.java)
        // Inflate the layout for this fragment
        binding.addbutton.setOnClickListener {
            val text = binding.editTextTextPersonName.text.toString()
            if(text.isNotEmpty()){
                viewModel.addtask(task(text))
            }
        }
        binding.taskrecyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = context?.let { taskAdapter(it,this) }
        binding.taskrecyclerview.adapter = adapter
        viewModel.alltask.observe(this, {
            if (adapter != null) {
                adapter.updatelist(it)
            }
        })


        return binding.root
    }

    override fun ontaskclicked(task: task) {
        viewModel.deletenotes(task)
        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
    }
}