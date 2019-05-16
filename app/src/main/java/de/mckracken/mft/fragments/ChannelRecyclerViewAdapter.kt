package de.mckracken.mft.fragments

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import de.mckracken.mft.model.Channel
import de.mckracken.mft.R

class ChannelRecyclerViewAdapter(val context : Context, var channelList : List<Channel>) : RecyclerView.Adapter<ChannelRecyclerViewAdapter.ChannelViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelRecyclerViewAdapter.ChannelViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false)
        val channelViewHolder = ChannelViewHolder(view)

        channelViewHolder.item.setOnClickListener(View.OnClickListener {
            var position = channelViewHolder.adapterPosition

            channelList.get(position).toggleReserved()
            notifyItemChanged(position)
        })

        return channelViewHolder
    }

    override fun getItemCount(): Int {
        return channelList.size
    }

    override fun onBindViewHolder(holder: ChannelRecyclerViewAdapter.ChannelViewHolder, position: Int) {
        var channel = channelList.get(position)
        var iconName : String = if (channel.reserved) "ic_circle_crossed" else "ic_circle_checked"

        holder.textView.text = channel.number.toString()
        holder.imageView.setImageResource(context.resources.getIdentifier(iconName, "drawable", "de.mckracken.mft"))
        holder.imageView.setColorFilter( if (channel.reserved) context.getColor(R.color.colorIconCrossed) else context.getColor(R.color.colorIconChecked))
        holder.item.setBackgroundColor( if (channel.reserved) context.getColor(R.color.colorChannelReserved) else context.getColor(R.color.colorChannelFree) )
    }

    inner class ChannelViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var item : ConstraintLayout
        var textView : TextView
        var imageView : ImageView

        init {
            item = itemView .findViewById(R.id.item_channel_cl)
            textView = itemView.findViewById(R.id.item_channel_tv)
            imageView = itemView.findViewById(R.id.item_channel_iv)
        }


    }

    override fun getSectionName(position: Int): String {
        return (position / 25 * 25).toString()
    }
}