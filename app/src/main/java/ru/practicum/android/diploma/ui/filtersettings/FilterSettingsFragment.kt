package ru.practicum.android.diploma.ui.filtersettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_filter_settings,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        requireActivity().findViewById<TextInputLayout>(R.id.textlayout_vacancy_type).setEndIconOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterIndustryFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FilterSettingsFragment.
         */
        @JvmStatic
        fun newInstance(
            param1: String,
            param2: String
        ) =
            FilterSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        ARG_PARAM1,
                        param1
                    )
                    putString(
                        ARG_PARAM2,
                        param2
                    )
                }
            }
    }
}
