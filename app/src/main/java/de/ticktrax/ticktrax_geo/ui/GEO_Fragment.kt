package de.ticktrax.ticktrax_geo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.ticktrax.ticktrax_geo.location.LocationService
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentGEOBinding

class GEO_Fragment : Fragment() {
    private lateinit var binding: FragmentGEOBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentGEOBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.geo.observe(viewLifecycleOwner){
            binding.GeoLocTV?.text = it.latitude.toString() + "/"+ it.longitude.toString() + "/"+ it.altitude.toString()

        }
        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(GEO_FragmentDirections.actionGEOFragment2ToExportFragment2())
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(GEO_FragmentDirections.actionGEOFragment2ToLocationsFragment())
        }


    }
}
