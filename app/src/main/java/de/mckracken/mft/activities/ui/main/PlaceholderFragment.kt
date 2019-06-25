package de.mckracken.mft.activities.ui.main

import android.app.Activity
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.fragments.NewBluetoothDeviceRecyclerViewAdapter
import de.mckracken.mft.manager.NewBluetoothManager
import kotlinx.android.synthetic.main.fragment_bluetooth_devices.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var bluetoothManager: NewBluetoothManager


    private val devices : ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var recyclerViewAdapter : NewBluetoothDeviceRecyclerViewAdapter
    private val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("BluetoothActivity()", "BluetoothDevice.ACTION_FOUND")
                    devices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                    recyclerViewAdapter.setDevices(devices)
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val renamedDevice : BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("BluetoothActivity", "BluetoothDevice.ACTION_NAME_CHANGED: " + renamedDevice.name)
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

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        bluetoothManager.discoverDevices()
        activity?.registerReceiver(receiver, filter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = (activity?.application as MultinoxApplication).bluetoothManager
        recyclerViewAdapter = NewBluetoothDeviceRecyclerViewAdapter(activity as Activity, ArrayList(), bluetoothManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth_devices, container, false)

        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0

        if(sectionNumber == 1)
            recyclerViewAdapter.setDevices(devices)
        else
            recyclerViewAdapter.setDevices(bluetoothManager.showPairedDevices() ?: ArrayList())

        view.available_devices_rv.layoutManager = LinearLayoutManager(activity as Activity)
        view.available_devices_rv.addItemDecoration(DividerItemDecoration(activity as Activity, DividerItemDecoration.VERTICAL))
        view.available_devices_rv.adapter = recyclerViewAdapter

        return view
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}