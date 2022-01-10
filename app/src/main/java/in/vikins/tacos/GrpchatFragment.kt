package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentChatBinding
import `in`.vikins.tacos.databinding.FragmentGrpchatBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GrpchatFragment : Fragment() {
    private lateinit var binding:FragmentGrpchatBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grpchat, container, false)
    }

}