package ru.practicum.android.diploma.ui.filterlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterLocationBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterLocationFragment : BindingFragment<FragmentFilterLocationBinding>() {

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterLocationBinding {
        return FragmentFilterLocationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textVacancyLocationTitle.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        }
    }
}
