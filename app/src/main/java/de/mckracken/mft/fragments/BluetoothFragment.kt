package de.mckracken.mft.fragments

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.R
import de.mckracken.mft.manager.CustomBluetoothManager
import kotlinx.android.synthetic.main.fragment_bluetooth.view.*

class BluetoothFragment : Fragment() {

    private lateinit var bluetoothManager : CustomBluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = CustomBluetoothManager(activity as Context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        val devicesArray : ArrayList<BluetoothDevice> = bluetoothManager.getDevices() ?: ArrayList()
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_bluetooth_rv)
        val recyclerViewAdapter = BluetoothDeviceRecyclerViewAdapter(activity as Activity, devicesArray, bluetoothManager)
        recyclerView.adapter = recyclerViewAdapter
        val blReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    // add device to the list
                    devicesArray.add(device)
                    recyclerViewAdapter.setDevices(devicesArray)
                }
            }
        }
        bluetoothManager.setReceiver(blReceiver)

        view.fragment_bluetooth_button_on.setOnClickListener { bluetoothManager.bluetoothOn(activity as AppCompatActivity) }
        view.fragment_bluetooth_button_off.setOnClickListener { bluetoothManager.bluetoothOff() }
        view.fragment_bluetooth_button_discover.setOnClickListener { bluetoothManager.discoverDevices() }
        view.fragment_bluetooth_button_show.setOnClickListener { recyclerViewAdapter.setDevices(bluetoothManager.showPairedDevices() ?: devicesArray) }


//        bluetoothIcon.setOnClickListener {
//            if (bluetoothManager.isBluetoothEnabled()) bluetoothManager.bluetoothOff() else bluetoothManager.bluetoothOn(context as AppCompatActivity)
//        }




        return view

    }
}