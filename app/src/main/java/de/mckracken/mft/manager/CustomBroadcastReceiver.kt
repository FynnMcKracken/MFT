package de.mckracken.mft.manager

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

//const val TAG = "CustomBroadcastReceiver"

class CustomBroadcastReceiver(val context: Context?) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                BluetoothAdapter.STATE_OFF -> Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show()
                BluetoothAdapter.STATE_ON -> Toast.makeText(context, "Bluetooth is on", Toast.LENGTH_SHORT).show()
            }
        }
    }
}