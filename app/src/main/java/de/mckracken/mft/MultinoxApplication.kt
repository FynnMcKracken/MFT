package de.mckracken.mft

import android.app.Application
import de.mckracken.mft.model.Device

class MultinoxApplication : Application() {
    var devices = mutableListOf(
        Device("LED Lamp", "Location maybe?", 0, 100,0),
        Device("RGB Lamp", "Some info about the rgb lamp", 200, 100, 1),
        Device("Laser", "Too long text should be truncated...", 400, 100,2),
        Device("Apache Helicopter", "Pew pew pew", 500, 5, 3)
    )
}