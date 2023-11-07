package de.ticktrax.ticktrax_geo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlaceDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationDetailItemBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentPlacesDetailBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentPlacesDetailItemBinding
import de.ticktrax.ticktrax_geo.myTools.formatDate4Recycler

class OSMPlaceDetailsAdapter(
    private val thisOSMPLaceDetailS:  List<OSMPlaceDetail>
) : RecyclerView.Adapter<OSMPlaceDetailsAdapter.ItemViewHolder>() {


    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentPlacesDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            FragmentPlacesDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val binding = holder.binding
        var myOSMPlaceDetail = thisOSMPLaceDetailS[position.toInt()]
        Log.d("ufe-adapter", "in LocationDetailsAdapter " + myOSMPlaceDetail
        )
        binding.textViewDateFrom.text= formatDate4Recycler(myOSMPlaceDetail.firstSeen)
        binding.textViewDateTo.text= formatDate4Recycler(myOSMPlaceDetail.lastSeen)
        binding.textViewDuration.text= myOSMPlaceDetail.durationMinutes.toString()+"m"
    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisOSMPLaceDetailS.size
    }
}

