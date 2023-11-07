package de.ticktrax.ticktrax_geo.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentPlacesItemBinding
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.Places_FragmentDirections
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class OSMPlaceAdapter(
    private val thisOSMPlaces: List<OSMPlace>
) : RecyclerView.Adapter<OSMPlaceAdapter.ItemViewHolder>() {

    private lateinit var map: MapView

    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentPlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       // logDebug("ufe", "create viewholder")
        val binding =
            FragmentPlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val binding = holder.binding
        // Hole die Somedata aus dem enveloppe
        var myOSMPlace = thisOSMPlaces[position.toInt()]
        binding.LonLatTV!!.text = myOSMPlace.lat.toString() + "/" + myOSMPlace.lon
        binding.DisplayNameTV!!.text = myOSMPlace.displayName

        map = binding.placesItemMAP!!
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
        val startZoom = 14.5
        val startPoint = GeoPoint(myOSMPlace.lat!!.toDouble(), myOSMPlace.lon!!.toDouble());
        mapController.setZoom(startZoom)
        mapController.setCenter(startPoint);
        // My Location Overlay

        // Im Fragment
//        val context: Context = requireContext()
        val context: Context = holder.itemView.context
        val currentIcon =
            AppCompatResources.getDrawable(context, R.drawable.baseline_my_location_24)
        // Convert Drawable to Bitmap
        val iconBitmap = currentIcon?.toBitmap()
        val marker = Marker(map)
        marker.position = startPoint
        marker.icon = currentIcon
        marker.title = "Place"
        marker.snippet = ""

        map.overlays.add(marker)


        val myLocationoverlay = MyLocationNewOverlay(map)
        //myLocationoverlay.setPersonIcon(iconBitmap)
        // map.getOverlays().add(myLocationoverlay);
        //  myLocationoverlay.enableFollowLocation()
        //    myLocationoverlay.enableMyLocation()


        binding.thePlacesCardView.setOnClickListener {
          //  logDebug("ufe", "on the way to osm place detail with $position")
            holder.binding.root.findNavController()
                .navigate(
                    //Places_FragmentDirections.actionPlacesFragmentToPlacesDetailFragment(position)
                    Places_FragmentDirections.actionPlacesFragmentToPlacesDetailFragment(position)
                )
        }

    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisOSMPlaces.size
    }
}

