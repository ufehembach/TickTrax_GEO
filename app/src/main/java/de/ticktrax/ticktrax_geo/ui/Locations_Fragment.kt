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
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationsBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.adapter.OSMPlaceAdapter
import de.ticktrax.ticktrax_geo.ui.adapter.TTLocationAdapter


class Locations_Fragment : Fragment() {
    private lateinit var binding: FragmentLocationsBinding
    private val viewModel: TickTraxViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.GEO_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
          navController.navigate(R.id.places_Fragment)

        }
        viewModel.ttLocationExtS.observe(viewLifecycleOwner) {
         //   logDebug("ufe", "Call Adapter ${it}")
         //   logDebug("ufe", "${it.size}")
            binding.locationRV?.adapter = TTLocationAdapter(it)
        }

        // Der SnapHelper sorgt dafür, dass die RecyclerView immer auf das aktuelle List Item springt
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.locationRV)

//       //Create function to navigate, because we have access to the navController in the fragment
//        //This function can be passed to the adapter
//        val navigateFunction: ((data: Data) -> Unit) = {
//            //findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment(it.text,it.textId,it.number))
//            findNavController().navigate(ExportFragementDirections.actionFirstFragmentToSecondFragment(it.text,it.textId,it.number))
//        }
    }
}