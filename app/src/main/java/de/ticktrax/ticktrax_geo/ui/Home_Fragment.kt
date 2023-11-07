package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeBinding


class Home_Fragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.places_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.export2Mail_Fragment)
        }


        // viewModel.osmPlaceS.observe(viewLifecycleOwner) {
        // logDebug("ufe", "Call Adapter ${it}")
        // logDebug("ufe", "${it.size}")
        //     binding.homeRV?.adapter = AggregationAdapter(it)
        //  }

        // Der SnapHelper sorgt dafür, dass die RecyclerView immer auf das aktuelle List Item springt
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.homeRV)

    }

    fun showLogFrag() {
        binding.root.findNavController()
            .navigate(R.id.ALogFragment)
    }
}
