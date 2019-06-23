package de.mckracken.mft.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.mckracken.mft.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExpertFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExpertFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExpertFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expert, container, false)
    }
}
