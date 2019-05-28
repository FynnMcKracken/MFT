package de.mckracken.mft.manager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.UnsupportedEncodingException

class CustomBluetoothManager (applicationContext: Context) {
    private var bluetoothService: BluetoothService? = null
    private var message : String? = null
    private var connectionStatus : String? = null
    private var bluetoothStatus : String? = null
    private var devicesArray : ArrayList<BluetoothDevice>? = null
    private var blReceiver : BroadcastReceiver? = null
    private val applicationContext : Context = applicationContext

    init {
        initializeBluetoothService()
    }

    private fun initializeBluetoothService(){
        val handler = object: Handler() {
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    MESSAGE_READ -> messageRead(msg)
                    MESSAGE_CONNECTION -> messageConnection(msg)
                }
            }
        }
        bluetoothService = BluetoothService(handler)
    }

    // TODO: Not sure if activity should be supplied here or as class-argument/property
    fun bluetoothOn(activity: AppCompatActivity){
        if (bluetoothService?.enabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
//        bluetoothStatus = "Bluetooth enabled"
        Toast.makeText(applicationContext, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
    }
    fun bluetoothOff(){
        bluetoothService?.disable()
//        bluetoothStatus = "Bluetooth disabled"
        Toast.makeText(applicationContext, "Bluetooth disabled", Toast.LENGTH_SHORT).show()
    }
    fun isBluetoothEnabled() : Boolean {
        return  bluetoothService != null && bluetoothService?.enabled == true
    }

    fun messageRead(msg: Message){
        try {
            message = msg.obj as String
            // TODO: Alert some Listener that a new message was received?
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
    fun messageConnection(msg: Message){
        if (msg.arg1 == 1)
            connectionStatus = "Connected to Device: ${msg.obj as String}"
        else
            connectionStatus = "Connection Failed"
    }
    fun getMessage() : String? {
        return message
    }
    fun getConnectionStatus() : String? {
        return connectionStatus
    }

    fun getDevices() : ArrayList<BluetoothDevice>? {
        return devicesArray
    }

    fun setReceiver(receiver: BroadcastReceiver) {
        blReceiver = receiver
    }

    fun discoverDevices(){
        if (bluetoothService?.enabled!!) {
            bluetoothService?.discover()
            Toast.makeText(applicationContext, "Discovering Paired Devices", Toast.LENGTH_SHORT).show()
            devicesArray?.clear()
            applicationContext.registerReceiver(blReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        } else {
            Toast.makeText(applicationContext, "Bluetooth not on", Toast.LENGTH_SHORT).show()
        }
    }

    fun showPairedDevices() : ArrayList<BluetoothDevice>? {
        if (bluetoothService?.enabled!!) {
            for (device in bluetoothService?.pairedDevices!!)
                devicesArray?.add(device)

            Toast.makeText(applicationContext, "Show Paired Devices", Toast.LENGTH_SHORT).show()
            return devicesArray
        } else
            Toast.makeText(applicationContext, "Bluetooth not on", Toast.LENGTH_SHORT).show()
            return devicesArray

    }

    fun connect(macAddress: String) {
        connectionStatus = "Connecting"
        bluetoothService?.connect(macAddress)
    }

    fun messageToast(msg: Message){
        Toast.makeText(applicationContext, msg.obj as String, Toast.LENGTH_SHORT).show()
    }



}