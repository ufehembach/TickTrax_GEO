package de.ticktrax.ticktrax_geo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.ticktrax.ticktrax_geo.location.LocationService
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.databinding.FragmentGEOBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

class GEO_Fragment : Fragment() {
    private lateinit var binding: FragmentGEOBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    private lateinit var map: MapView
    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentGEOBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Configuration.getInstance().setUserAgentValue(context?.getPackageName() ?: "TickTrax.de");
        map = binding.geoMAP!!
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
        val startZoom = 18.5

        mapController.setZoom(startZoom)
        val startPoint = GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        viewModel.ttLocation.observe(viewLifecycleOwner) {
            val mytext = it.lat.toString() + "/" + it.lon.toString() + "/" + it.alt.toString()
            binding.GeoLocTV?.text = mytext
            logDebug("ufe-geo", "map view for : " + mytext)
           // viewModel.aLog(ALogType.GEO, "show Map: " + mytext)
            //mapController.setZoom(9.5)
            mapController.setZoom(startZoom)
            var currentLoc = GeoPoint(it.lat, it.lon);
            mapController.setCenter(currentLoc);
         //   val context: Context = holder.itemView.context
            val context = requireContext ()
            val currentIcon =
                AppCompatResources.getDrawable(context, R.drawable.baseline_my_location_24)
            // Convert Drawable to Bitmap
            val iconBitmap = currentIcon?.toBitmap()
            val marker = Marker(map)
            marker.position = currentLoc
            marker.icon = currentIcon
            marker.title = "Place"
            marker.snippet = ""

            map.overlays.add(marker)
        }
        binding.nextFAB.setOnClickListener {
            R.layout.fragment_g_e_o
            val navController = findNavController()
            navController.navigate(R.id.export2Mail_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.locations_Fragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        map.getTileProvider().clearTileCache();
    }

    override fun onStop() {
        super.onStop()
        map.getTileProvider().clearTileCache();
    }
}
