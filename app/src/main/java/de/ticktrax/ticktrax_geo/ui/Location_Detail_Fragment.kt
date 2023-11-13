package de.ticktrax.ticktrax_geo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationDetailBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel
import de.ticktrax.ticktrax_geo.ui.adapter.TTLocationAdapter
import de.ticktrax.ticktrax_geo.ui.adapter.TTLocationDetailsAdapter
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Location_Detail_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Location_Detail_Fragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val viewModel: TickTraxViewModel by activityViewModels()
    private lateinit var binding: FragmentLocationDetailBinding

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
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        logDebug("ufe", " locationdetails oncreateview ")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDebug("ufe", " settings fields ")
        var a = viewModel
        var b = a.locationExtS
        var c = b.value
        var d = c?.get(index)

        // map
        var mapView = binding.locDetailMAP

        mapView.controller.setZoom(18.5)

        val customLocation = GeoPoint(d!!.ttLocation.lat.toDouble(), d!!.ttLocation.lon.toDouble())
        // Breite der Karte in Pixel
        val mapWidth = mapView.width
        // Zentriere die Karte um 30% nach links verschoben
        // Prozentuale Verschiebung (30% nach links)
        val offsetPercent = 0.3

        // Zoom-Level der Karte
        val zoomLevel = mapView.zoomLevelDouble

        // Längengradbreite in Grad

        // Berechne die Verschiebung basierend auf Zoom und Längengradbreite
        val offset = (offsetPercent * mapWidth * mapWidth / (zoomLevel * 360)).toFloat()

        // Zentriere die Karte mit der Verschiebung
        val mapCenter = GeoPoint(
            customLocation.latitude,
            customLocation.longitude - offset
        )
        mapView.controller.setCenter(mapCenter)

        // Füge einen Marker mit Textoverlay hinzu
        val marker = Marker(mapView)
        marker.position = customLocation
        marker.title = "Custom Location"
        marker.snippet = "Your custom text here"

        // Füge den Marker zur Karte hinzu
        mapView.overlays.add(marker)

        // Zeige die Karte an
        mapView.invalidate()

        // rv
        viewModel.locationDetailS4IdSetId(d.ttLocation.LocationId)
        viewModel.locationDetailS4Id.observe(viewLifecycleOwner) {
            logDebug("ufe-detail", "Call Adapter ${it}")
            logDebug("ufe-detail", "${it.size}")
            binding.recyclerView.adapter = TTLocationDetailsAdapter(it)
        }
        // scroll view

    //    viewModel.osmPlace4LonLatSetLonLat(d.ttLocation.lon.toDouble(), d.ttLocation.lat.toDouble())
        viewModel.osmPlace4LonLat.observe(viewLifecycleOwner) {
            var osm = viewModel.osmPlace4LonLat.value
            logDebug(
                "ufe-detail", d.ttLocation.lon.toString() + " " + d.ttLocation.lat
            )
            if (osm != null) {
                binding.placeIdTV?.text = osm.OSMPlaceId.toString()
                binding.licenseTV?.text = osm.licence
                binding.osmTypeTV?.text = osm.osmType.toString()
                binding.osmIdTV?.text = osm.osmId.toString()
                binding.latTV?.text = osm.lat.toString()
                binding.lonTV?.text = osm.lon.toString()
                binding.classTV?.text = osm.osmClass.toString()
                binding.typeTV?.text = osm.type.toString()
                binding.placeRankTV?.text = osm.placeRank.toString()
                binding.importanceTV?.text = osm.importance.toString()
                binding.addressTypeTV?.text = osm.addresstype.toString()
                binding.nameTV?.text = osm.name
                binding.displayNameTV?.text = osm.displayName
                binding.houseNumberTV?.text = osm.houseNumber
                binding.roadTV?.text = osm.road
                binding.hamletTV?.text = osm.hamlet
                binding.townTV?.text = osm.town
                binding.countyTV?.text = osm.county
                binding.stateTV?.text = osm.state
                binding.iso3166lvl4TV?.text = osm.ISO3166
                binding.postcodeTV?.text = osm.postcode
                binding.countryTV?.text = osm.country
                binding.countryCodeTV?.text = osm.countryCode
                binding.boundingBox0TV?.text = osm.bb0
                binding.boundingBox1TV?.text = osm.bb1
                binding.boundingBox2TV?.text = osm.bb2
                binding.boundingBox3TV?.text = osm.bb3
            }
        }
    }
}