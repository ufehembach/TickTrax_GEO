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
import de.ticktrax.ticktrax_geo.ui.adapter.GenericAdapter
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeBinding


class Home_Fragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TemplateViewModel by activityViewModels()

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
            navController.navigate(Home_FragmentDirections.actionHomeFragment2ToExportFragment2())
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(Home_FragmentDirections.actionHomeFragment2ToSettingsFragment2())
        }

        binding.btnRefresh?.setOnClickListener{
            Log.d("ufe","refreshwhat pressed")
            viewModel.reloadGenericEnv()
        }
        viewModel.genericEnv.observe(viewLifecycleOwner) {
            Log.d("ufe", "Call Adapter ${it}")
            Log.d("ufe", "${it.media.size}")
            binding.rvGeneric?.adapter = GenericAdapter(it)
        }
        viewModel.genericEnv.observe(viewLifecycleOwner) {
            Log.d("ufe", "Call Adapter ${it}")
            //   Log.d("ufe", "${it.media.size}")
            binding.rvGeneric?.adapter = GenericAdapter(it)
        }
        // Der SnapHelper sorgt dafür, dass die RecyclerView immer auf das aktuelle List Item springt
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.rvGeneric)

    }
}
