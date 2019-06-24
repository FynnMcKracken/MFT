package de.mckracken.mft.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiagnosticsViewModel : ViewModel() {

    var diagnose: LiveData<Boolean?> = MutableLiveData<Boolean>().apply { postValue(false) }

}