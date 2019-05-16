package de.mckracken.mft.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import de.mckracken.mft.MainActivity
import de.mckracken.mft.model.Channel
import de.mckracken.mft.R

class ListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_list, container, false)
        var recyclerView = view.findViewById<FastScrollRecyclerView>(R.id.recyclerview)
        var channelList = List(512) { Channel(it, true) }
        var recyclerViewAdapter = ChannelRecyclerViewAdapter(activity as MainActivity, channelList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        recyclerView.adapter = recyclerViewAdapter

        return view
    }
}