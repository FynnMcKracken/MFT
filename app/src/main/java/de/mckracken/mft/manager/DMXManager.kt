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
        const val END_SEQUENCE : String = "\r\n"

        fun getModePaket(n : Byte) : ByteArray {
            return "{INIT_MODE}{n}{END_SEQUENCE}".toByteArray()
        }

        fun getIPAdressPaket(byte1 : Byte, byte2 : Byte, byte3 : Byte, byte4 : Byte) : ByteArray {
            return "{INIT_IP}{byte1}.{byte2}.{byte3}.{byte4}{END_SEQUENCE}".toByteArray()
        }

        fun getDMXPaket(channel : Short, value : Byte) : ByteArray {
            return "{INIT_DMX}{channel}{value}{END_SEQUENCE}".toByteArray()
        }

        fun getLockPaket(channel : Short, locked : Boolean) : ByteArray {
            return "{INIT_LOCK}{channel}{locked}{END_SEQUENCE}".toByteArray()
        }

    }
}