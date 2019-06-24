package de.mckracken.mft.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.mckracken.mft.model.Channel

class ChannelsViewModel : ViewModel() {

    val channels = List<MutableLiveData<Channel>>(512) {MutableLiveData()}

    init {
        for (i in channels.indices) channels[i].value = Channel(i, 0, i % 3 == 0)
    }

    fun setChannelValue(channel : Int, value : Int) {
        val tempReserved = channels[channel-1].value?.reserved ?: false
        channels[channel-1].value = Channel(channel-1, value, tempReserved)

        Log.d("ChannelsViewModel", "Channel " + channel + " changed to " + value)
    }

    fun setChannelLocked(channel : Int, locked : Boolean) {
        val tempValue = channels[channel-1].value?.value ?: 0
        channels[channel-1].value = Channel(channel-1, tempValue, locked)

        Log.d("ChannelsViewModel", "Channel " + channel + " locked == " + locked)
    }

    fun getChannel(channel : Int) : MutableLiveData<Channel> {
        return channels[channel-1]
    }

}