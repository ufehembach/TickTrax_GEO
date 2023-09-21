package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import de.ticktrax.ticktrax_geo.databinding.FragmentSettingsBinding


class Settings_Fragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(Settings_FragmentDirections.actionSettingsFragment2ToHomeFragment2())
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(Settings_FragmentDirections.actionSettingsFragment2ToMeFragment2())

//       //Create function to navigate, because we have access to the navController in the fragment
//        //This function can be passed to the adapter
//        val navigateFunction: ((data: Data) -> Unit) = {
//            //findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment(it.text,it.textId,it.number))
//            findNavController().navigate(ExportFragementDirections.actionFirstFragmentToSecondFragment(it.text,it.textId,it.number))
//        }
//
//        val dataset: List<Data> = Datasource().generateList()
//        val adapter = DataAdapter(dataset, requireContext(), navigateFunction)
//        binding.dataRV.adapter = adapter
//        binding.dataRV.setHasFixedSize(true)
        }
    }
}