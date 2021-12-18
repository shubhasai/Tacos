package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.FragmentExploreBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController

class ExploreFragment : Fragment() {
    lateinit var binding:FragmentExploreBinding
    lateinit var url:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        url = "https://www.hackerearth.com/challenges/hackathon/"
        webviewloading()
        siteselection()
        return binding.root
    }
    fun siteselection()
    {
        binding.hackathonsiteselction.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.devfolio -> {
                        url = "https://devfolio.co/hackathons"
                        webviewloading()
                    }
                    R.id.devpost -> {
                        url = "https://devpost.com/hackathons"
                        webviewloading()
                    }
                    R.id.hackerearth -> {
                        url = "https://www.hackerearth.com/challenges/hackathon/"
                        webviewloading()
                    }
                }
            } else {
                if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                    url = "https://devpost.com/hackathons"
                    webviewloading()
                }
            }
        }
    }
    fun shouldInterceptBackPress() = true
    fun webviewloading(){
        val webv = binding.hackathonwebview
        webv.webViewClient = WebViewClient()
        webv.loadUrl(url)
        webv.settings.javaScriptEnabled = true
        webv.settings.allowFileAccess = true
        webv.settings.allowContentAccess = true
        webv.settings.domStorageEnabled = true
        webv.settings.useWideViewPort = true
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress()){
                    if(webv.canGoBack()){
                        webv.goBack()
                    }
                    else{
                        val direction = ExploreFragmentDirections.actionExploreFragmentToHomeFragment()
                        findNavController().navigate(direction)
                    }

                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }
}