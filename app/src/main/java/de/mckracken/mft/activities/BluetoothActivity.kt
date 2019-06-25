package de.mckracken.mft.activities

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.fragments.NewBluetoothDeviceRecyclerViewAdapter
import de.mckracken.mft.manager.NewBluetoothManager
import kotlinx.android.synthetic.main.activity_bluetooth.*

class BluetoothActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        bluetoothManager = (application as MultinoxApplication).bluetoothManager

        setSupportActionBar(bluetooth_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Bluetooth"

        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)

        bluetooth_rv.layoutManager = LinearLayoutManager(this)
        bluetooth_rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerViewAdapter = NewBluetoothDeviceRecyclerViewAdapter(this, ArrayList(), bluetoothManager)
        bluetooth_rv.adapter = recyclerViewAdapter

        bluetooth_button_on.setOnClickListener { bluetoothManager.bluetoothOn(this) }
        bluetooth_button_off.setOnClickListener {
            bluetooth_spinner.visibility = View.INVISIBLE
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.bluetoothOff()
        }
        bluetooth_button_discover.setOnClickListener {
            bluetooth_spinner.visibility = View.VISIBLE
            bluetooth_spinner.bringToFront()
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.discoverDevices()
        }
        bluetooth_button_show.setOnClickListener {
            bluetooth_spinner.visibility = View.VISIBLE
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            recyclerViewAdapter.setDevices(bluetoothManager.showPairedDevices() ?: ArrayList())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_bluetooth, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.bluetooth_rv)
        val spinner = view.findViewById<ProgressBar>(R.id.bluetooth_spinner)
        recyclerViewAdapter =
            NewBluetoothDeviceRecyclerViewAdapter(activity, ArrayList(), bluetoothManager)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = recyclerViewAdapter

        view.bluetooth_button_on.setOnClickListener { bluetoothManager.bluetoothOn(activity) }
        view.bluetooth_button_off.setOnClickListener {
            spinner.visibility = View.INVISIBLE
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.bluetoothOff()
        }
        view.bluetooth_button_discover.setOnClickListener {
            spinner.visibility = View.VISIBLE
            spinner.bringToFront()
            devices.clear()
            recyclerViewAdapter.setDevices(devices)
            bluetoothManager.discoverDevices()
        }
        view.bluetooth_button_show.setOnClickListener {
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

    }*/

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(receiver, filter)
    }
}