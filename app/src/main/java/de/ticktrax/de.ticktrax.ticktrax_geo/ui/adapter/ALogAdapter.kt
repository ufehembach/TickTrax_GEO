package de.ticktrax.ticktrax_geo.ui.adapter

import DateTimeFormats
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.ALog
import de.ticktrax.de.ticktrax.ticktrax_geo.myTools.DateTimeUtils
import de.ticktrax.ticktrax_geo.ALogFragmentDirections
import de.ticktrax.ticktrax_geo.databinding.FragmentALogItemBinding
import java.util.Date

class ALogAdapter(
    private val thisALog: List<ALog>
) : RecyclerView.Adapter<ALogAdapter.ItemViewHolder>() {

    // stellt einen Listeneintrag dar
    inner class ItemViewHolder(val binding: FragmentALogItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // hier werden neue ViewHolder erstellt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d("ufe", "ALOG Adapter create viewholder")
        val binding =
            FragmentALogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // hier findet der Recyclingprozess statt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        Log.d("ufe", "ALOG Adapter onviewcreated")
        val binding = holder.binding
        // Hole die Somedata aus dem enveloppe
        var myALog = thisALog[position.toInt()]
        Log.d("ufe", "ALOG onbindviewholder " + position)
        var myDate = DateTimeUtils.parseDateTimeFromUTC(myALog.dateTime.toString())
        var niceDateString = DateTimeFormats.formatDateTime(myDate)
        //DateTimeUtils.formatDateTime(DateTimeUtils.parseDateTimeFromUTC(myALog.dateTime.toString()))

        binding.ALogItemTV.text =
            niceDateString + " " + myALog.logType.toString() + " " + myALog.logText.toString()
        //Use Coil to load images
//        Log.d("ufe", "get image from " + genericData.image)
//       binding.genericIV.load(genericData.image) {
//            transformations(RoundedCornersTransformation(10F))

        binding.theCardView.setOnClickListener {
            Log.d("ufe", "ALOG Adapter on the way to detail with $position")
            holder.binding.root.findNavController()
                .navigate(
                    ALogFragmentDirections.actionALogFragmentToALogDetailFragment(position)

                )
        }
    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        //return enericEnv.media.size
        return thisALog.size
    }
}

