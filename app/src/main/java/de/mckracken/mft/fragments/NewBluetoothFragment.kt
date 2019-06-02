package de.mckracken.mft.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.MainActivity
import de.mckracken.mft.R
import de.mckracken.mft.manager.NewBluetoothManager
import kotlinx.android.synthetic.main.fragment_bluetooth.view.*

class NewBluetoothFragment(private val activity : MainActivity) : Fragment() {

    private lateinit var bluetoothManager : NewBluetoothManager
    private val devices : ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var recyclerViewAdapter : NewBluetoothDeviceRecyclerViewAdapter
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.i("NewBluetoothFragment()", "BluetoothDevice.ACTION_FOUND")
                    devices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                    recyclerViewAdapter.setDevices(devices)
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val renamedDevice : BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i("NewBluetoothFragment", "BluetoothDevice.ACTION_NAME_CHANGED: " + renamedDevice.name)
                    for (device in devices) {
                        if (device.address == renamedDevice.address) {
                            devices[devices.indexOf(device)] = renamedDevice
                            recyclerViewAdapter.setDevices(devices)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = NewBluetoothManager(activity as Context)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        activity.registerReceiver(receiver, filter)
        Log.i("NewBluetoothFragment", "onCreate()")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_bluetooth_rv)
        val spinner = view.findViewById<ProgressBar>(R.id.fragment_bluetooth_spinner)
        recyclerViewAdapter = NewBluetoothDeviceRecyclerViewAdapter(activity, ArrayList(), bluetoothManager)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = recyclerViewAdapter

        view.fragment_bluetooth_button_on.setOnClickListener { bluetoothManager.bluetoothOn(activity) }
        view.fragment_bluetooth_button_off.setOnClickListener {
            spinner.visibility = View.INVISIBLE
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.bluetoothOff()
        }
        view.fragment_bluetooth_button_discover.setOnClickListener {
            spinner.visibility = View.VISIBLE
            spinner.bringToFront()
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.discoverDevices()
        }
        view.fragment_bluetooth_button_show.setOnClickListener {
            spinner.visibility = View.VISIBLE
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            recyclerViewAdapter.setDevices(bluetoothManager.showPairedDevices() ?: ArrayList())
        }






        return view

    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unregisterReceiver(receiver)
        Log.i("NewBluetoothFragment", "onDestroy()")
    }
}