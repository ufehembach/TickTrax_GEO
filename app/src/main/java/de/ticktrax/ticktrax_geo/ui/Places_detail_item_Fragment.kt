package de.ticktrax.ticktrax_geo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ticktrax.ticktrax_geo.R

/**
 * A simple [Fragment] subclass.
 * Use the [places_detail_item.newInstance] factory method to
 * create an instance of this fragment.
 */
class Places_Detail_Item_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(de.ticktrax.ticktrax_geo.ARG_PARAM1)
//            param2 = it.getString(de.ticktrax.ticktrax_geo.ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_detail_item, container, false)
    }


}