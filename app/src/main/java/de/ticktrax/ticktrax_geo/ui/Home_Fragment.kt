package de.ticktrax.ticktrax_geo.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeBinding
import de.ticktrax.ticktrax_geo.myTools.formatDate4Recycler
import de.ticktrax.ticktrax_geo.myTools.generateMeaningfulName
import de.ticktrax.ticktrax_geo.myTools.logError
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.MapView


class Home_Fragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TickTraxViewModel by activityViewModels()
    private lateinit var map: MapView
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

        //val filteredList =
        //    viewModel.osmPlaceExtS.value?.filter { it.osmPlace.osmPlaceId == viewModel.osmPlace.value?.osmPlaceId }
        //viewModel.osmPlaceExtS.observe(viewLifecycleOwner)
        viewModel.osmPlaceExt.observe(viewLifecycleOwner)
        {
            try {
                var myosmPlaceExt = it

                if (myosmPlaceExt != null) {
                    binding.DisplayNameTV!!.text =generateMeaningfulName(myosmPlaceExt.osmPlace.name.toString(),myosmPlaceExt.osmPlace.displayName.toString())
                    binding.firstSeenTV!!.text = formatDate4Recycler( myosmPlaceExt.osmPlace.firstSeen)
                    binding.lastSeenTV!!.text =formatDate4Recycler( myosmPlaceExt.osmPlace.lastSeen)
                    binding.durationTV!!.text = myosmPlaceExt.durationMinutes.toString()+"m"
                }
                var startPoint = GeoPoint(0.0,0.0 );
                //map
                map = binding.HomeMAP!!
                map.setTileSource(TileSourceFactory.MAPNIK)
                val mapController = map.controller
                val startZoom = 14.5
                startPoint = GeoPoint(
                    myosmPlaceExt?.osmPlace?.lat!!.toDouble(),
                    myosmPlaceExt?.osmPlace?.lon!!.toDouble()
                );
                mapController.setZoom(startZoom)
                mapController.setCenter(startPoint);
                // My Location Overlay

                // Im Fragment
                val context: Context = requireContext()
                val currentIcon =
                    AppCompatResources.getDrawable(context, R.drawable.baseline_location_on_24)
                // Convert Drawable to Bitmap
                val iconBitmap = currentIcon?.toBitmap()
                val marker = Marker(map)
                marker.position = startPoint
                marker.icon = currentIcon
                marker.title = "Place"
                marker.snippet = ""

                map.overlays.add(marker)
                val myLocationoverlay = MyLocationNewOverlay(map)
                var osm = myosmPlaceExt?.osmPlace!!

                binding.latTV?.text = osm.lat.toString()
                binding.lonTV?.text = osm.lon.toString()
                binding.typeTV?.text = osm.osmType.toString()
                binding.addressTypeTV?.text = osm.addresstype.toString()

                binding.nameTV?.text = osm.name
                binding.houseNumberTV?.text = osm.houseNumber
                binding.roadTV?.text = osm.road
                binding.townTV?.text = osm.town
                binding.stateTV?.text = osm.state
                binding.iso3166lvl4TV?.text = osm.ISO3166
                binding.postcodeTV?.text = osm.postcode
                binding.countryTV?.text = osm.country
                binding.countryCodeTV?.text = osm.countryCode
            } catch (e: Exception) {
                logError("ufe", "Nix gefunden")
            }
        }
        fun showLogFrag() {
            binding.root.findNavController()
                .navigate(R.id.ALogFragment)
        }
    }
}
