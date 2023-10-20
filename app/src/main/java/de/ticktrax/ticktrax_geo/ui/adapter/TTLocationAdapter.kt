package de.ticktrax.ticktrax_geo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.data.datamodels.TTLocation
import de.ticktrax.ticktrax_geo.databinding.FragmentLocationsItemBinding
import de.ticktrax.ticktrax_geo.ui.Locations_FragmentDirections

class TTLocationAdapter(
    private val thisTTLocations: List<TTLocation>
) : RecyclerView.Adapter<TTLocationAdapter.ItemViewHolder>() {


    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentLocationsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d("ufe", "create viewholder")
        val binding =
            FragmentLocationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        Log.d("ufe", "onviewcreated")
        val binding = holder.binding
        // Hole die Somedata aus dem enveloppe
        var myTTLocation = thisTTLocations[position.toInt()]
        Log.d("ufe", "onbindviewholder " + position)
        binding.LonLatTV!!.text = myTTLocation.lat.toString() + "/" + myTTLocation.lon
        binding.DisplayNameTV!!.text = myTTLocation.lastSeen.toString()

        //Use Coil to load images
//        Log.d("ufe", "get image from " + genericData.image)
//       binding.genericIV.load(genericData.image) {
//            transformations(RoundedCornersTransformation(10F))

        binding.theLocationCardView.setOnClickListener {
            Log.d("ufe", "on the way to detail with $position")
            holder.binding.root.findNavController()
                .navigate(
                    //Home_FragmentDirections.actionHomeFragment2ToHomeRecyclerDetail(position)
                    Locations_FragmentDirections.actionLocationsFragmentToLocationDetailFragment()
                )
        }

    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisTTLocations.size
    }
}

