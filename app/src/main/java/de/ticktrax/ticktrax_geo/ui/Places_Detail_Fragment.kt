package de.ticktrax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentPlacesDetailBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel
import de.ticktrax.ticktrax_geo.ui.adapter.OSMPlaceDetailsAdapter
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class Places_Detail_Fragment : Fragment() {

    private val viewModel: TickTraxViewModel by activityViewModels()
    private lateinit var binding: FragmentPlacesDetailBinding

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
        binding = FragmentPlacesDetailBinding.inflate(inflater, container, false)
        logDebug("ufe", " locationdetails oncreateview ")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDebug("ufe", " settings fields ")
        var a = viewModel
        var b = a.osmPlaceExtS
        var c = b.value
        var d = c?.get(index)
        var osm = d!!.osmPlace

        // map
        var mapView = binding.locDetailMAP
        mapView.controller.setZoom(18.5)

        val customLocation =
            GeoPoint(osm.lat!!.toDouble(), osm.lon!!.toDouble())
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
        viewModel.OSMPLacesDetailS4IdSetId(osm.osmPlaceId!!)
        viewModel.osmPlaceDetailS4Id.observe(viewLifecycleOwner) {
            logDebug("ufe-detail", it.size.toString() + " items for Adapter ${it}")
            binding.locationiDetailRV.adapter = OSMPlaceDetailsAdapter(it)
        }
        // scroll view

        viewModel.osmPlaceExtS.observe(viewLifecycleOwner) {
            binding.placeIdTV?.text = osm.osmPlaceId.toString()
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
