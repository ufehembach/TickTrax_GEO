package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentPlacesBinding
import de.ticktrax.ticktrax_geo.ui.adapter.OSMPlaceAdapter

class Places_Fragment : Fragment() {
    private lateinit var binding: FragmentPlacesBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.locations_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            //navController.navigate(Me_FragmentDirections.actionMeFragment2ToGEOFragment2())
            navController.navigate(R.id.home_Fragment)
        }
        viewModel.osmPlaceExtS.observe(viewLifecycleOwner) {
          //  logDebug("ufe", "Call Adapter ${it}")
          //  logDebug("ufe", "${it.size}")
            binding.placesRV?.adapter = OSMPlaceAdapter(it)
        }

        // Der SnapHelper sorgt dafür, dass die RecyclerView immer auf das aktuelle List Item springt
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.placesRV)
    }
}