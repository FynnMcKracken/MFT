package de.mckracken.mft.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.mckracken.mft.R
import kotlinx.android.synthetic.main.fragment_diagnostics.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DiagnosticsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DiagnosticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DiagnosticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostics, container, false)

        view.diagnostics_start_button.setOnClickListener {
            view.diagnostics_check_mark.check()
        }

        return view
    }

}
