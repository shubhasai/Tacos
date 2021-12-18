package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentCreategrpBinding
import `in`.vikins.tacos.databinding.FragmentGroupsBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController


class GroupsFragment : Fragment() {
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
        return binding.root
    }

}