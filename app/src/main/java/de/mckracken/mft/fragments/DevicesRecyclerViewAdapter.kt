package de.mckracken.mft.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.R
import de.mckracken.mft.model.Device
import kotlinx.android.synthetic.main.item_channel.view.*

class DevicesRecyclerViewAdapter(val context : Context, val devicesList : List<Device>) : RecyclerView.Adapter<DevicesRecyclerViewAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeviceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false))

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) = holder.bind(devicesList[position])

    override fun getItemCount(): Int = devicesList.size

    inner class DeviceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
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