package de.ticktrax.ticktrax_g

import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel

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
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeDetailBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeItemBinding

class Home_Detail_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val ViewModel: TickTraxViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeDetailBinding

    private lateinit var myOSMPlace: OSMPlace
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
        Log.d("ufe", " settings fields ")
        var a = ViewModel
        var b = a.osmPlaces
        var c = b.value
        var d = c?.get(index)

        if (d != null) {
            binding.placeIdTV?.text = d.placeId.toString()
            binding.licenseTV?.text = d.licence
            binding.osmTypeTV?.text = d.osmType.toString()
            binding.osmIdTV?.text = d.osmId.toString()
            binding.latTV?.text = d.lat.toString()
            binding.lonTV?.text = d.lon.toString()
            binding.classTV?.text = d.osmClass.toString()
            binding.typeTV?.text = d.type.toString()
            binding.placeRankTV?.text=d.placeRank.toString()
            binding.importanceTV?.text=d.importance.toString()
            binding.addressTypeTV?.text=d.addresstype.toString()
            binding.nameTV?.text=d.name
            binding.displayNameTV?.text=d.displayName
            binding.houseNumberTV?.text=d.houseNumber
            binding.roadTV?.text=d.road
            binding.hamletTV?.text=d.hamlet
            binding.townTV?.text=d.town
            binding.countyTV?.text=d.county
            binding.stateTV?.text=d.state
            binding.iso3166lvl4TV?.text=d.ISO3166
            binding.postcodeTV?.text=d.postcode
            binding.countryTV?.text=d.country
            binding.countryCodeTV?.text=d.countryCode
            binding.boundingBox0TV?.text=d.bb0
            binding.boundingBox1TV?.text=d.bb1
            binding.boundingBox2TV?.text=d.bb2
            binding.boundingBox3TV?.text=d.bb3
        }

//        binding.imageIV.load(ViewModel.genericEnv.value!!.media[index].image) {
//            transformations(RoundedCornersTransformation(10F))

//        val videoView: VideoView = binding.genericVV
//        videoView.setVideoPath(ViewModel.genericEnv.value!!.media[index].movie)
//        Log.d("ufe", "loading " + ViewModel.genericEnv.value!!.media[index].movie)
//        videoView.start()

        //binding.backToHomeButton.setOnClickListener {
        //    val navController = this.findNavController()

        //    navController.navigateUp()
        // }
    }
}