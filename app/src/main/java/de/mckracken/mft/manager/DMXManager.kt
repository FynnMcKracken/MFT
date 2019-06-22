package de.mckracken.mft.manager

class DMXManager {

    fun handlePaket(byteArray : ByteArray) {
        when (byteArray[0].toChar()) {
            'T' -> handleDiagnosisResponsePaket(byteArray)
            'C' -> handleDMXPaket(byteArray)
            'L' -> handleLockPaket(byteArray)
        }
    }

    private fun handleDiagnosisResponsePaket(byteArray: ByteArray) {
        if (testSuccess(byteArray)) {
            // TODO: alert some listener of success
        } else {
            // TODO: alert some listener of failure
        }
    }

    private fun testSuccess(b : ByteArray) : Boolean {
        return b[1].toInt() == 1
    }

    private fun handleDMXPaket(byteArray : ByteArray) {
        val channel = (byteArray[1] + byteArray[2]).toShort()
        val value = byteArray[3].toInt()
        // TODO: alert some listener to change the DMX-Channel to value
    }

    private fun handleLockPaket(byteArray : ByteArray) {
        val channel = (byteArray[1] + byteArray[2]).toShort()
        val locked = byteArray[3].toInt() == 1
        // TODO: alert some listener to lock or unlock channel
    }

    companion object {

        const val INIT_MODE : Char = 'M'
        const val INIT_IP : Char = 'I'
        const val INIT_DMX : Char = 'C'
        const val INIT_LOCK : Char = 'L'
        const val END_SEQUENCE : Char = 'X'
        const val DMX_SEPARATOR : Char = '/'

        fun getModePaket(n : Byte) : ByteArray {
            return "$INIT_MODE$n$END_SEQUENCE".toByteArray(Charsets.US_ASCII)
        }

        fun getIPAdressPaket(address : String) : ByteArray {
            val substrings = address.split('.')
            var fixedLengthAddress = ""
            for (string : String in substrings) {
                fixedLengthAddress += string.padStart(3, '0')
            }
            return ("$INIT_IP$fixedLengthAddress$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getDMXPaket(channel : Short, value : Byte) : ByteArray {
            return ("$INIT_DMX" + "$channel".padStart(3, '0') + "$DMX_SEPARATOR" + "$value".padStart(3, '0') + "$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

        fun getLockPaket(channel : Short, locked : Boolean) : ByteArray {
            return ("$INIT_LOCK" + "$channel".padStart(3, '0') + "$locked$END_SEQUENCE").toByteArray(Charsets.US_ASCII)
        }

    }
}