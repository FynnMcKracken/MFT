package de.mckracken.mft.fragments

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.mckracken.mft.MainActivity
import de.mckracken.mft.R
import de.mckracken.mft.util.InputFilterMinMax
import de.mckracken.mft.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.fragment_expert.view.*

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expert, container, false)
        val viewModel = ViewModelProviders.of(context as MainActivity).get(ChannelsViewModel::class.java)
        view.channel_input_edit_text.filters = arrayOf<InputFilter>(InputFilterMinMax(1, 512))
        view.channel_info_edit_text.filters = arrayOf<InputFilter>(InputFilterMinMax(0, 255))

        view.channel_input_edit_text.doOnTextChanged { text, start, count, after ->
            if (text?.isNotEmpty() == true) {
                val index = text.toString().toInt()
                view.channel_info_edit_text.setText(viewModel.getChannel(index).value?.value.toString())
                view.edit_chip.isChecked = viewModel.getChannel(index).value?.reserved ?: false
            }
        }

        view.edit_chip.setOnCheckedChangeListener { buttonView, isChecked ->
            view.channel_info_edit_text.isEnabled = isChecked
        }

        return view
    }
}
