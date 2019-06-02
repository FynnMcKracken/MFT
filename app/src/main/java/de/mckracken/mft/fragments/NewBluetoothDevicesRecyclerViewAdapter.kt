package de.mckracken.mft.fragments

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.R
import de.mckracken.mft.manager.NewBluetoothManager
import kotlinx.android.synthetic.main.item_bluetooth_device.view.*

class NewBluetoothDeviceRecyclerViewAdapter(val context: Context, var devicesArray: ArrayList<BluetoothDevice>, val bluetoothManager: NewBluetoothManager)  : RecyclerView.Adapter<NewBluetoothDeviceRecyclerViewAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeviceViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.item_bluetooth_device, parent, false))

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) = holder.bind(devicesArray[position])

    override fun getItemCount(): Int {
        return devicesArray.size
    }

    fun setDevices(devices: ArrayList<BluetoothDevice>) {
        devicesArray = devices
        notifyDataSetChanged()
    }

    inner class DeviceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item : ConstraintLayout = itemView.findViewById(R.id.device_layout)

        fun bind(device: BluetoothDevice) = with(itemView) {
            device_name_textview.text = device.name
            device_address_textview.text = device.address

            setOnClickListener {
                Toast.makeText(context, "Connecting", Toast.LENGTH_SHORT).show()
                // Get the device MAC address, which is the last 17 chars in the View
                val info = device.address
                val macAddress = info.substring(info.length - 17)
                bluetoothManager.connect(macAddress)
            }
        }
    }
}