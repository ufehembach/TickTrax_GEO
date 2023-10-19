package de.ticktrax.ticktrax_geo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.ticktrax_geo.data.datamodels.OSMPlace
import de.ticktrax.ticktrax_geo.databinding.FragmentHomeItemBinding
import de.ticktrax.ticktrax_geo.ui.Home_FragmentDirections
import de.ticktrax.ticktrax_geo.ui.Home_Item_Fragment

class AggregationAdapter(
    private val thisOSMPlaces: List<OSMPlace>
) : RecyclerView.Adapter<AggregationAdapter.ItemViewHolder>() {


    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d("ufe", "create viewholder")
        val binding =
            FragmentHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        Log.d("ufe", "onviewcreated")
        val binding = holder.binding
        // Hole die Somedata aus dem enveloppe
        var myOSMPlace = thisOSMPlaces[position.toInt()]
        Log.d("ufe", "onbindviewholder " + position)
        binding.LonLatTV!!.text = myOSMPlace.lat.toString() + "/" + myOSMPlace.lon
        binding.DisplayNameTV!!.text = myOSMPlace.displayName

        //Use Coil to load images
//        Log.d("ufe", "get image from " + genericData.image)
//       binding.genericIV.load(genericData.image) {
//            transformations(RoundedCornersTransformation(10F))

        binding.theHomeCardView?.setOnClickListener {
            Log.d("ufe", "on the way to detail with $position")
            holder.binding.root.findNavController()
                .navigate(
                    Home_FragmentDirections.actionHomeFragmentToHomeRecyclerDetail(position)
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

