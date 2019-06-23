package de.mckracken.mft.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.activities.AddDeviceActivity
import kotlinx.android.synthetic.main.fragment_devices.*
import kotlinx.android.synthetic.main.fragment_devices.view.*


class DevicesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_devices, container, false)
        view.devices_recyclerView.layoutManager = LinearLayoutManager(context)
        view.devices_recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.devices_recyclerView.adapter = DevicesRecyclerViewAdapter(activity as Activity, MultinoxApplication().devices)
        setHasOptionsMenu(true)

        view.add_device_fab.setOnClickListener {
            startActivity(Intent(this.context, AddDeviceActivity::class.java))
        }

        return view
    }

    override fun onResume() {
        devices_recyclerView?.adapter = DevicesRecyclerViewAdapter(activity as Activity, MultinoxApplication().devices)
        Log.d("DEVICES RESUME", MultinoxApplication().devices.toString())
        super.onResume()
    }

    /*override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_devices, menu)
    }*/
}