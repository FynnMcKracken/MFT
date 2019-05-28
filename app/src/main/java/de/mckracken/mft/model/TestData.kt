package de.mckracken.mft.model

object TestData {
    val Devices : List<Device> = listOf(
        Device("RGB Lamp", "Some info about the rgb lamp", 0, 6, 10),
        Device("LED Lamp", "Location maybe?", 16, 12,1),
        Device("Laser", "Too long text should be truncated...", 124, 4,2),
        Device("Apache Helicopter", "Pew pew pew", 465, 10, 3))
}