package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.SearchState

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: VacancyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )
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

        binding.viewmodel = viewModel
        adapter = VacancyAdapter { id: String -> openVacancy(id) }

        viewModel.observeSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Start -> showStart()
                is SearchState.Content -> showContent(state.data)
                is SearchState.Loading -> showLoading()
                is SearchState.Error -> showError(state.type)
            }
        }

    }

    private fun openVacancy(id: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            bundleOf(VACANCY_KEY to id)
        )
    }

    private fun showContent(data: List<VacancyLight>) {
        adapter.setItems(data)

    }

    private fun showLoading() {
        setStartVisibility(false)
        setProgressVisibility(true)
        setErrorVisibility(false)
        setListVisibility(false)
    }

    private fun showError(errorType: ErrorType) {
        setResultVisibility(false)
        setErrorVisibility(true)
        setStartVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)

        when (errorType) {
            ErrorType.EMPTY -> {
                binding.imageError.setImageResource(R.drawable.empty_results_cat)
                binding.textError.text = getString(R.string.can_not_get_vacancies)
            }

            ErrorType.NO_CONNECTION -> {
                binding.imageError.setImageResource(R.drawable.skull)
                binding.textError.text = getString(R.string.no_internet_connection)

            }

            ErrorType.SERVER_ERROR -> {
                binding.imageError.setImageResource(R.drawable.search_server_error)
                binding.textError.text = getString(R.string.server_error)

            }
        }

    }

    private fun showStart() {
        setResultVisibility(false)
        setStartVisibility(true)
        setProgressVisibility(false)
        setErrorVisibility(false)
        setListVisibility(false)

    }

    private fun setStartVisibility(isVisible: Boolean) {
        binding.imageStart.isVisible = isVisible

    }

    private fun setErrorVisibility(isVisible: Boolean) {
        binding.layoutError.isVisible = isVisible

    }

    private fun setListVisibility(isVisible: Boolean) {
        binding.recyclerViewVacancies.isVisible = isVisible

    }

    private fun setResultVisibility(isVisible: Boolean) {
        binding.textResult.isVisible = isVisible

    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    companion object {
        const val VACANCY_KEY = "vacancy"
    }
}
