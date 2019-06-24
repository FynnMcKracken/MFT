package de.mckracken.mft

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import de.mckracken.mft.manager.CustomBroadcastReceiver
import de.mckracken.mft.manager.DMXManager
import de.mckracken.mft.manager.NewBluetoothManager
import de.mckracken.mft.model.Device

class MultinoxApplication : Application() {

    lateinit var bluetoothManager : NewBluetoothManager
    lateinit var dmxManager : DMXManager
    var brReceiver : BroadcastReceiver? = null

    var devices = mutableListOf(
        Device("LED Lamp", "Location maybe?", 0, 100,0),
        Device("RGB Lamp", "Some info about the rgb lamp", 200, 100, 1),
        Device("Laser", "Too long text should be truncated...", 400, 100,2),
        Device("Apache Helicopter", "Pew pew pew", 500, 5, 3)
    )

    override fun onCreate() {
        super.onCreate()
        /*dmxManager = DMXManager(this)
        bluetoothManager = NewBluetoothManager(this, dmxManager)*/
    }


}