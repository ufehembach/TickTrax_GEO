package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeItemBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeRecyclerDetailBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeRecyclerDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeRecyclerDetail : Fragment() {
    // TODO: Rename and change types of parameters

    private val ViewModel: TickTraxViewModel by activityViewModels()
    private lateinit var binding:  FragmentHomeRecyclerDetailBinding

    private lateinit var myOSMPlace: OSMPlace
    private var index: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt("position")
            logDebug("ufe", "set pos  " + index)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_home_recycler_detail, container, false)
        binding = FragmentHomeRecyclerDetailBinding.inflate(inflater, container, false)
        logDebug("ufe", " oncreateview ")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDebug("ufe", " settings fields ")
        var a = ViewModel
        var b = a.osmPlaceS
        var c = b.value
        var d = c?.get(index)

        if (d != null) {
            binding.placeIdTV?.text = d.OSMPlaceId.toString()
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
//        logDebug("ufe", "loading " + ViewModel.genericEnv.value!!.media[index].movie)
//        videoView.start()

        //binding.backToHomeButton.setOnClickListener {
        //    val navController = this.findNavController()

        //    navController.navigateUp()
        // }
    }

}