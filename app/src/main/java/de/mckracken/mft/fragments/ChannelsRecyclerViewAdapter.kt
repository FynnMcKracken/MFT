package de.mckracken.mft.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import de.mckracken.mft.R
import de.mckracken.mft.model.Channel
import de.mckracken.mft.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.item_channel_overview.view.*

class ChannelsRecyclerViewAdapter(val frag : Fragment, val channelsViewModel: ChannelsViewModel) : RecyclerView.Adapter<ChannelsRecyclerViewAdapter.ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChannelViewHolder(LayoutInflater.from(frag.context).inflate(R.layout.item_channel_overview, parent, false))

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) = holder.bind(channelsViewModel.channels[position])

    override fun getItemCount(): Int = channelsViewModel.channels.size

    inner class ChannelViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(channel: MutableLiveData<Channel>) = with(itemView) {
            itemView.channel_id_text_view.text = channel.value?.id.toString()

            channel.observe(frag, Observer {
                itemView.setBackgroundColor(
                    if (it.reserved)
                        ContextCompat.getColor(context, R.color.colorAccent)
                    else
                        ContextCompat.getColor(context, R.color.colorAccentSecondary)
                )
            })
        }
    }
}