package de.mckracken.mft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IPViewModel : ViewModel() {
    val ipAddress : MutableLiveData<String?> = MutableLiveData()
}