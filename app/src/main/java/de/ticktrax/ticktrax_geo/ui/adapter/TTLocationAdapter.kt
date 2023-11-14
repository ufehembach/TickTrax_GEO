package de.ticktrax.ticktrax_geo.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationsItemBinding
import de.ticktrax.ticktrax_geo.myTools.formatDate4Recycler
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.Locations_FragmentDirections
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class TTLocationAdapter(
    private val thisTTLocationExtS: List<TTLocationExt>
) : RecyclerView.Adapter<TTLocationAdapter.ItemViewHolder>() {


    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentLocationsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            FragmentLocationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val binding = holder.binding
        // Hole die Somedata aus dem enveloppe
        var myTTLocation = thisTTLocationExtS[position.toInt()]
        binding.LonLatTV!!.text =
            myTTLocation.ttLocation.lat.toString() + "/" + myTTLocation.ttLocation.lon
        binding.DisplayNameTV!!.text =
            formatDate4Recycler (myTTLocation.ttLocation.lastSeen) + " Duration: " + myTTLocation.durationMinutes + "min"

        //map
        var map = binding.LocationItemMAP!!
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
        val startZoom = 14.5
        val startPoint = GeoPoint(myTTLocation.ttLocation.lat!!.toDouble(),myTTLocation.ttLocation.lon!!.toDouble());
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

        binding.theLocationCardView.setOnClickListener {
            logDebug("ufe", "on the way to TT location detail with $position")
            holder.binding.root.findNavController()
                .navigate(
                    Locations_FragmentDirections.actionLocationsFragmentToLocationDetailFragment(
                        position
                    )
                )
        }

    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisTTLocationExtS.size
    }
}

