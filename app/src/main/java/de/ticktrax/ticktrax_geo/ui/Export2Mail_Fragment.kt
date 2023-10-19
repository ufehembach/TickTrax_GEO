package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentExport2MailBinding

class Export2Mail_Fragment : Fragment() {
    private lateinit var binding: FragmentExport2MailBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentExport2MailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            //navController.navigate(Export_FragmentDirections.actionExportFragment2ToHomeFragment2())
           navController.navigate(R.id.home_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.GEO_Fragment)
        }
        val data = viewModel.osmPlaceS
        binding.exportDataTV?.text = data.toString ()
//        val gson = Gson()
//        val myGson = gson.toJson(data)
    }
}