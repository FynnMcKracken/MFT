package de.mckracken.mft.model

class Channel(val id : Int, var value : Int, var reserved : Boolean) {

    fun toggleReserved() {
        reserved = !reserved
    }
}