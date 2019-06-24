package de.mckracken.mft.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import de.mckracken.mft.R
import de.mckracken.mft.model.Channel

class ChannelFragment(private val channel : Channel, private val myContext : Context) : Fragment() {

    companion object {
        fun newInstance(channel : Channel, context : Context) : ChannelFragment {
            return ChannelFragment(channel, context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_channel, container, false)
        val icon = view.findViewById<ImageView>(R.id.fragment_channel_IV)
        val text = view.findViewById<TextView>(R.id.fragment_channel_TV)
        val outerLayout = view.findViewById<ConstraintLayout>(R.id.fragment_channel_CL1)

        icon.setImageResource(if (channel.reserved) R.drawable.ic_circle_crossed else R.drawable.ic_circle_checked)
//        icon.setColorFilter( if (channel.reserved) myContext.getColor(R.color.colorIconCrossed) else myContext.getColor(R.color.colorIconChecked))
        text.text = channel.id.toString()
//        outerLayout.setBackgroundColor(if (channel.reserved) myContext.getColor(R.color.colorIconCrossed) else myContext.getColor(R.color.colorIconChecked))
        return view
    }

}