package de.mckracken.mft.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.manager.DMXManager
import de.mckracken.mft.viewmodel.DiagnosticsViewModel
import kotlinx.android.synthetic.main.fragment_diagnostics.view.*

class DiagnosticsFragment(val viewModel: DiagnosticsViewModel) : Fragment() {

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostics, container, false)

        viewModel.diagnose.observe(this, Observer {
            if(it == true)
                view.diagnostics_check_mark.check()
            else
                view.diagnostics_check_mark.visibility = View.GONE
        })

        view.diagnostics_start_button.setOnClickListener {
            (activity?.application as MultinoxApplication).bluetoothManager.write(DMXManager.getModePacket(0))
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        (activity?.application as MultinoxApplication).bluetoothManager.write(DMXManager.getModePacket(1))
    }

}
