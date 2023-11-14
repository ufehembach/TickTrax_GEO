package de.ticktrax.ticktrax_geo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import de.ticktrax.ticktrax_geo.databinding.FragmentALogBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentALogItemBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel
import de.ticktrax.ticktrax_geo.ui.adapter.ALogAdapter


class ALogFragment : Fragment() {
    private lateinit var binding: FragmentALogBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
   //     logDebug("ufe","oncreateview ALOG")
        binding = FragmentALogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.alogDataS.observe(viewLifecycleOwner) {
     //       logDebug("ufe", "Call ALOG Adapter ${it}")
     //       logDebug("ufe", "${it.size}")
            binding.rvLog.adapter = ALogAdapter(it)
        }

        // Der SnapHelper sorgt dafür, dass die RecyclerView immer auf das aktuelle List Item springt
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.rvLog)
    }
}