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
import de.mckracken.mft.model.TestData


class ListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<FastScrollRecyclerView>(R.id.recyclerview)
        val devicesList = TestData.Devices
        val recyclerViewAdapter = ChannelRecyclerViewAdapter(activity as MainActivity, devicesList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = recyclerViewAdapter

        return view
    }
}