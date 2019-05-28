package de.mckracken.mft.fragments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import de.mckracken.mft.R
import de.mckracken.mft.model.Device
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelRecyclerViewAdapter(val context : Context, val devicesList : List<Device>) : RecyclerView.Adapter<ChannelRecyclerViewAdapter.ChannelViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChannelViewHolder(LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false))

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) = holder.bind(devicesList[position])

    override fun getItemCount(): Int {
        return devicesList.size
    }

    override fun getSectionName(position: Int): String {
        return (position / 25 * 25).toString()
    }

    inner class ChannelViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item : ConstraintLayout = itemView.findViewById(R.id.device_layout)

        fun bind(device: Device) = with(itemView) {
            device_title_textview.text = device.title
            device_info_textview.text = device.info
            channel_start_textView.text = """${device.channelStart}"""
            channel_end_textView.text = (device.channelStart + device.channelWidth - 1).toString()
            device_icon.setImageResource(
                when(device.icon) {
                    0 -> R.mipmap.led_bulb
                    1 -> R.mipmap.rgb_lamp
                    2 -> R.mipmap.laser_beam
                    3 -> R.mipmap.apache_helicopter
                    else -> R.mipmap.led_bulb
                })
            setOnClickListener { }
        }
    }
}