package de.ticktrax.ticktrax_geo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationDetail
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocationExt
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationDetailItemBinding
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationsItemBinding
import de.ticktrax.ticktrax_geo.myTools.formatDate4Recycler
import de.ticktrax.ticktrax_geo.myTools.logDebug
import de.ticktrax.ticktrax_geo.ui.Locations_FragmentDirections

class TTLocationDetailsAdapter(
    private val thisTTLocationDetailS:  List<TTLocationDetail>
) : RecyclerView.Adapter<TTLocationDetailsAdapter.ItemViewHolder>() {


    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentLocationDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            FragmentLocationDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val binding = holder.binding
        var myTTLocationDetail = thisTTLocationDetailS[position.toInt()]
        Log.d("ufe-adapter", "in LocationDetailsAdapter " + myTTLocationDetail
        )
        binding.textViewDateFrom.text= formatDate4Recycler(myTTLocationDetail.firstSeen)
        binding.textViewDateTo.text= formatDate4Recycler(myTTLocationDetail.lastSeen)
        binding.textViewDuration.text= myTTLocationDetail.durationMinutes.toString()+"m"
    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisTTLocationDetailS.size
    }
}

