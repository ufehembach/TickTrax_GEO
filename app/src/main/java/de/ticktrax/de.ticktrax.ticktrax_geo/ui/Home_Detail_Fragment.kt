package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.datamodels.GenericData
import de.ticktrax.ticktrax_geo.data.datamodels.GenericEnvelope
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeDetailBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeItemBinding

class Home_Detail_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val ViewModel: TemplateViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeDetailBinding

    private lateinit var myGenericEnv: GenericEnvelope
    private lateinit var myGenericData: GenericData
    private var index: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt("position")
            Log.d("ufe", "set pos  " + index)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        Log.d("ufe", " oncreateview ")
        return binding.root
        //return inflater.inflate(R.layout.fragment_home_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ufe", " settingMovieTV ")
        var a = ViewModel
        var b = a.genericEnv
        var c = b.value
        var d = c!!.media
        var e = d[index]
        var f = e.movietext
        binding.genericDetailTV.text = ViewModel.genericEnv.value!!.media[index].movietext
        binding.genericDetailTV.text = ViewModel.genericEnv.value!!.media[index].imagetext
        binding.imageIV.load(ViewModel.genericEnv.value!!.media[index].image) {
            transformations(RoundedCornersTransformation(10F))
        }
        val videoView: VideoView = binding.genericVV
        videoView.setVideoPath(ViewModel.genericEnv.value!!.media[index].movie)
        Log.d("ufe", "loading " + ViewModel.genericEnv.value!!.media[index].movie)
        videoView.start()

        //binding.backToHomeButton.setOnClickListener {
        //    val navController = this.findNavController()

        //    navController.navigateUp()
        // }
    }
}