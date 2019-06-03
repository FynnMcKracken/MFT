package de.mckracken.mft.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity;
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
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = null

        saveButton.setOnClickListener {
            val deviceName = deviceNameEditText.text.toString()
            val deviceInfo = deviceInfoEditText.text.toString()
            val channelStart = firstChannelEditText.text.toString()
            val channelWidth = channelWidthEditText.text.toString()
            val device = Device(deviceName, deviceInfo, channelStart.toInt(), channelStart.toInt() + channelWidth.toInt(), 0)

            Log.d("ADD DEVICE ACTIVITY", "Adding device $deviceName")

            TestData.Devices.plus(device)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

}
