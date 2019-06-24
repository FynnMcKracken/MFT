package de.mckracken.mft.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val bluetoothDevice : MutableLiveData<BluetoothDevice?> = MutableLiveData<BluetoothDevice?>()

}