package de.ticktrax.ticktrax_geo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentALogBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentALogDetailBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel

class ALogDetailFragment : Fragment() {

    private val ViewModel: TickTraxViewModel by activityViewModels()
    private lateinit var binding: FragmentALogDetailBinding

    private lateinit var myALog: ALog
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
        binding = FragmentALogDetailBinding.inflate(inflater, container, false)
        logDebug("ufe", " oncreateview ")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDebug("ufe", " settings fields ")
        var a = ViewModel
        var b= a.alogDataS
        var c = b.value
        var d = c?.get(index)

        if (d != null) {
            binding.ALogDetailTV?.text = d.dateTime + " " + d.logType + " " + d.logText + " " + d.logDetail
        }
    }
}