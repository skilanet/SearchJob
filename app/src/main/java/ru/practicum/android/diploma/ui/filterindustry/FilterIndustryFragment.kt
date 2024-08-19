package ru.practicum.android.diploma.ui.filterindustry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding

class FilterIndustryFragment : Fragment() {
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterIndustryViewModel by viewModel()
    private val adapter by lazy {
        FilterIndustryAdapter { viewModel.onChecked(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterIndustryBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        binding.recyclerIndustries.adapter = adapter

        viewModel.observeItems().observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
