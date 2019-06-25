package de.mckracken.mft.manager

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import de.mckracken.mft.MainActivity
import de.mckracken.mft.viewmodel.ChannelsViewModel
import de.mckracken.mft.viewmodel.DiagnosticsViewModel
import de.mckracken.mft.viewmodel.IPViewModel

class DMXManager (context : Context) {

    private val channelViewModel : ChannelsViewModel = ViewModelProviders.of(context as MainActivity).get(ChannelsViewModel::class.java)
    private val diagnosticsViewModel : DiagnosticsViewModel = ViewModelProviders.of(context as MainActivity).get(DiagnosticsViewModel::class.java)
    private val ipAddressViewModel : IPViewModel = ViewModelProviders.of(context as MainActivity).get(IPViewModel::class.java)

    fun handlePacket(packet : String) {
        Log.d("DMXManager", "handlePacket: " + packet)
        when (packet[0]) {
            'T' -> handleDiagnosisResponsePacket(packet)
            'C' -> handleDMXPacket(packet)
            'L' -> handleLockPacket(packet)
            'I' -> handleIPPacket(packet)
        }
    }

    private fun handleIPPacket(packet: String) {
        if (packet.length > 12) {
            val first = packet.substring(1, 4).toInt()
            val second = packet.substring(4, 7).toInt()
            val third = packet.substring(7, 10).toInt()
            val fourth = packet.substring(10, 13).toInt()
            val address = "$first.$second.$third.$fourth"
            ipAddressViewModel.ipAddress.postValue(address)
        }
    }

    private fun handleDiagnosisResponsePacket(packet : String) {
        val diagnose = packet[1].toString().toInt() == 1
        if (diagnosticsViewModel.diagnose.value != diagnose)
            if (packet.length > 1) diagnosticsViewModel.diagnose.postValue(diagnose)
    }

    private fun testSuccess(packet : String) : Boolean {
        return packet[1].toInt() == 1
    }


    private fun handleDMXPacket(packet: String) {
        val substrings = packet.split('C', '/', 'X').filter {it.isNotEmpty()}
        Log.d("DMXManager", "substrings: " + substrings)
        if (substrings.size > 1) {
            val channel = substrings[0].toInt()
            val value = substrings[1].toInt()
            channelViewModel.setChannelValue(channel, value)
        }
    }

    private fun handleLockPacket(packet : String) {
        val substrings = packet.split('L', '/', 'X').filter {it.isNotEmpty()}
        if (substrings.size > 1) {
            val channel = substrings[0].toInt()
            val locked = substrings[1].toInt() == 1
            channelViewModel.setChannelLocked(channel, locked)
        }
    }

    companion object {

        const val INIT_START : Char = 'S'
        const val INIT_MODE : Char = 'M'
        const val INIT_IP : Char = 'I'
        const val INIT_DMX : Char = 'C'
        const val INIT_LOCK : Char = 'L'
        const val INIT_DISPLAY : Char = 'D'
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
            return ("$INIT_LOCK" + "$channel".padStart(3, '0') + "$DMX_SEPARATOR" + (if (locked) "1" else "0") + "$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getDisplayPacket(on : Boolean) : ByteArray {
            return ("$INIT_DISPLAY$" + (if (on) '1' else '0') + "$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getInitializationPacket() : ByteArray {
            return ("$INIT_START" + "1" + "$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

    }
}