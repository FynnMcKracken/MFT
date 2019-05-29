package de.mckracken.mft.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.mckracken.mft.model.Device
import de.mckracken.mft.model.TestData

class DevicesViewModel : ViewModel() {
    val devices: MutableLiveData<Device> by lazy {
        MutableLiveData<Device>()
    }

}