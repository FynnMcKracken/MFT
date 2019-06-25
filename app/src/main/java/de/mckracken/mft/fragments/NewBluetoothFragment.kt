package de.mckracken.mft.fragments

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.MainActivity
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.manager.DMXManager
import de.mckracken.mft.manager.NewBluetoothManager
import de.mckracken.mft.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.fragment_bluetooth.view.*

class NewBluetoothFragment(private val activity : MainActivity) : Fragment() {

    private var bluetoothManager = (activity.application as MultinoxApplication).bluetoothManager
    private val devices : ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var recyclerViewAdapter : NewBluetoothDeviceRecyclerViewAdapter
    private var dmxManager =  (activity.application as MultinoxApplication).dmxManager
    private val channelsViewModel = ViewModelProviders.of(activity).get(ChannelsViewModel::class.java)
    private val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("NewBluetoothFragment()", "BluetoothDevice.ACTION_FOUND")
                    devices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                    recyclerViewAdapter.setDevices(devices)
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val renamedDevice : BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("NewBluetoothFragment", "BluetoothDevice.ACTION_NAME_CHANGED: " + renamedDevice.name)
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
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        activity.registerReceiver(receiver, filter)
        channelsViewModel.getChannel(42).observe(activity, Observer { t ->
            //Toast.makeText(context, "Channel updated", Toast.LENGTH_SHORT).show()
            //Log.d("NewBluetoothFragment", "Channel updated")

        })
        Log.d("NewBluetoothFragment", "onCreate()")

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
        view.fragment_bluetooth_button_test_dmx.setOnClickListener {
            bluetoothManager.write(DMXManager.getLockPacket(42,true))
            bluetoothManager.write(DMXManager.getDMXPacket(42,42))
//            dmxManager.handlePacket(DMXManager.getDMXPacket(42,42).toString(Charsets.US_ASCII))
        }
        view.fragment_bluetooth_button_test_channel_42.setOnClickListener {
            Toast.makeText(context, "Channel 42: " + channelsViewModel.getChannel(42).value?.value, Toast.LENGTH_SHORT).show()
        }





        return view

    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        activity.registerReceiver(receiver, filter)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        activity.unregisterReceiver(receiver)
//        Log.i("NewBluetoothFragment", "onDestroy()")
//    }
}