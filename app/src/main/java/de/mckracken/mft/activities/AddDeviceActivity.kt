package de.mckracken.mft.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity;
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.model.Device
import de.mckracken.mft.model.TestData
import kotlinx.android.synthetic.main.activity_add_device.*
import kotlinx.android.synthetic.main.item_channel.*

class AddDeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)
        setSupportActionBar(add_device_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = null

        saveButton.setOnClickListener {
            val deviceName = deviceNameEditText.text.toString()
            val deviceInfo = deviceInfoEditText.text.toString()
            val channelStart = firstChannelEditText.text.toString()
            val channelWidth = channelWidthEditText.text.toString()
            val device = Device(deviceName, deviceInfo, channelStart.toInt(), channelStart.toInt() + channelWidth.toInt(), 0)

            Log.d("ADD DEVICE ACTIVITY", "Adding device $deviceName")

            Log.d("ADD DEVICE ACTIVITY", "Devices before: ${MultinoxApplication().devices.size}")

            var devices = MultinoxApplication().devices

            devices.add(device)

            MultinoxApplication().devices = devices

            Log.d("ADD DEVICE ACTIVITY", "Devices now: ${devices.size}")

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

}
