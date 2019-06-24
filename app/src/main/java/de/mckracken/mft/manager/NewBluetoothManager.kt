package de.mckracken.mft.manager

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

private const val PERMISSIONS_REQUEST_ID = 42
const val MESSAGE_READ: Int = 0
const val MESSAGE_CONNECTION: Int = 1
const val MESSAGE_TOAST: Int = 2

class NewBluetoothManager (val context: Context, val dmxManager: DMXManager) {

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val brReceiver = CustomBroadcastReceiver(context)
    // https://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3
    // Hint: If you are connecting to a Bluetooth serial board then try using the
    // well-known SPP UUID 00001101-0000-1000-8000-00805F9B34FB.
    // However if you are connecting to an Android peer then please generate your own unique UUID.
    val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    val handler = object: Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MESSAGE_CONNECTION -> messageConnection(msg)
                MESSAGE_TOAST -> messageToast(msg)
            }
        }
    }


    init {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Your Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        }
        context.registerReceiver(brReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    fun bluetoothOn(activity : Activity) {
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            Toast.makeText(context, "Bluetooth is already on", Toast.LENGTH_SHORT).show()
        }

    }

    fun bluetoothOff() {
        bluetoothAdapter?.disable()
    }

    fun showPairedDevices() : ArrayList<BluetoothDevice>? {
        Log.i("NewBluetoothManager", "bluetoothAdapter?.bondedDevices: " + bluetoothAdapter?.bondedDevices)
        return ArrayList(bluetoothAdapter?.bondedDevices ?: ArrayList())
    }

    fun discoverDevices() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_ID)

        } else {
            if (bluetoothAdapter?.isEnabled == true) {
                bluetoothAdapter?.cancelDiscovery()
                if (bluetoothAdapter?.startDiscovery() == true) {
                    Toast.makeText(context, "Discovering...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Cannot start Discovery", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun connect(macAddress: String) {
        bluetoothAdapter?.cancelDiscovery()
        val device = bluetoothAdapter?.getRemoteDevice(macAddress)

        device?.let {
            val connectThread = ConnectThread(it)
            connectThread.start()
        }
    }

    fun messageConnection(msg: Message){
        if (msg.arg1 == 1)
            Toast.makeText(context, "Connected to Device: ${msg.obj as String}", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show()
    }

    fun messageToast(msg: Message) {
        //TODO
    }



    private inner class ConnectThread(val device: BluetoothDevice) : Thread() {
        override fun run(){
            var fail = false

            var socket: BluetoothSocket? = null

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (e: IOException) {
                fail = true

                //Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show()
            }

            // Establish the Bluetooth socket connection.
            try {
                socket?.connect()
            } catch (e: IOException) {
                try {
                    fail = true
                    socket?.close()
                    handler.obtainMessage(MESSAGE_CONNECTION, -1, -1).sendToTarget()
                } catch (e2: IOException) {
                    //insert code to deal with this
                    // Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show()
                }

            }

            if (!fail) {
                if (socket != null) connectedThread = ConnectedThread(socket)
                connectedThread?.start()
                handler.obtainMessage(MESSAGE_CONNECTION, 1, -1, device.name).sendToTarget()
            }

        }

    }
    private var connectedThread: ConnectedThread? = null

    fun write(bytes : ByteArray) {
        connectedThread?.write(bytes)
    }

    fun cancelConnection() {
        connectedThread?.cancel()
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream = mmSocket.inputStream
        val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()
            val stringBuilder = StringBuilder()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                stringBuilder.append(mmBuffer.toString(Charsets.US_ASCII).substring(0, numBytes))
                Log.d("NewBluetoothManager", "InputStream: " + mmBuffer.toString(Charsets.US_ASCII).substring(0, numBytes))
                Log.d("NewBluetoothManager", "stringBuilder.toString(): " + stringBuilder.toString())
                if (stringBuilder.endsWith("X")){
                    // Send the obtained String to the DMX-Manager
                    Log.d("NewBluetoothManager", "DMX-Paket: " + stringBuilder.toString())
                    dmxManager.handlePacket((stringBuilder.toString()))
                    stringBuilder.clear()
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return

            }
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

}