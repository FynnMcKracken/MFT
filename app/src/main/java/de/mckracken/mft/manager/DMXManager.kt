package de.mckracken.mft.manager

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import de.mckracken.mft.viewmodel.ChannelsViewModel

class DMXManager (context : Context) {

    private val channelViewModel : ChannelsViewModel = ViewModelProviders.of(context as AppCompatActivity).get(ChannelsViewModel::class.java)

    fun handlePacket(packet : String) {
        Log.d("DMXManager", "handlePacket: " + packet)
        when (packet[0]) {
            'T' -> handleDiagnosisResponsePacket(packet)
            'C' -> handleDMXPacket(packet)
            'L' -> handleLockPacket(packet)
        }
    }

    private fun handleDiagnosisResponsePacket(packet : String) {
        if (testSuccess(packet)) {
            // TODO: alert some listener of success
        } else {
            // TODO: alert some listener of failure
        }
    }

    private fun testSuccess(packet : String) : Boolean {
        return packet[1].toInt() == 1
    }


    private fun handleDMXPacket(packet: String) {
        val substrings = packet.split('C', '/', 'X').filter {it.isNotEmpty()}
        Log.d("DMXManager", "substrings: " + substrings)
        val channel = substrings[0].toInt()
        val value = substrings[1].toInt()
        channelViewModel.setChannelValue(channel, value)
    }

    private fun handleLockPacket(packet : String) {
        val substrings = packet.split('L', '/', 'X').filter {it.isNotEmpty()}
        val channel = substrings[0].toInt()
        val locked = substrings[1].toInt() == 1
        channelViewModel.setChannelLocked(channel, locked)
    }

    companion object {

        const val INIT_MODE : Char = 'M'
        const val INIT_IP : Char = 'I'
        const val INIT_DMX : Char = 'C'
        const val INIT_LOCK : Char = 'L'
        const val END_SEQUENCE : Char = 'X'
        const val DMX_SEPARATOR : Char = '/'

        fun getModePacket(n : Byte) : ByteArray {
            return "$INIT_MODE$n$END_SEQUENCE".toByteArray(Charsets.US_ASCII)
        }

        fun getIPAdressPacket(address : String) : ByteArray {
            val substrings = address.split('.')
            var fixedLengthAddress = ""
            for (string : String in substrings) {
                fixedLengthAddress += string.padStart(3, '0')
            }
            return ("$INIT_IP$fixedLengthAddress$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getDMXPacket(channel : Short, value : Byte) : ByteArray {
            return ("$INIT_DMX" + "$channel".padStart(3, '0') + "$DMX_SEPARATOR" + "$value".padStart(3, '0') + "$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getLockPacket(channel : Short, locked : Boolean) : ByteArray {
            return ("$INIT_LOCK" + "$channel".padStart(3, '0') + "$locked$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

    }
}