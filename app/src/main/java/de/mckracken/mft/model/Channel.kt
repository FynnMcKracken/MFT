package de.mckracken.mft.model

class Channel(val number : Int, var reserved : Boolean) {

    fun toggleReserved() {
        reserved = !reserved
    }
}