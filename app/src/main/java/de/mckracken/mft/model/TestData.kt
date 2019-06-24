package de.mckracken.mft.model

import androidx.lifecycle.MutableLiveData
import java.util.*

class TestData {
    var Devices : MutableList<Device> = mutableListOf(
        Device("LED Lamp", "Location maybe?", 0, 100,0),
        Device("RGB Lamp", "Some info about the rgb lamp", 200, 100, 1),
        Device("Laser", "Too long text should be truncated...", 400, 100,2),
        Device("Apache Helicopter", "Pew pew pew", 500, 5, 3))
}