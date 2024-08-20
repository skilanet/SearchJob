package ru.practicum.android.diploma.ui.search

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.SearchState
import ru.practicum.android.diploma.util.BindingFragment
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by viewModel()
    private var adapter: VacancyAdapter? = null
    private val localeContext by lazy {
        val configuration = Configuration(this.requireContext().resources.configuration)
        configuration.setLocale(Locale("ru"))
        this.requireContext().createConfigurationContext(configuration)
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        setSearchIcon()

        binding.editTextSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEditorActionDone()
            }
            false
        }

        viewModel.observeSearchTextState().observe(viewLifecycleOwner) {
            updateTextInputLayoutIcon(it)
        }

        viewModel.observeFilterEnableState().observe(viewLifecycleOwner) {
            setFilterButtonFrame(it)
        }

        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }

        viewModel.observeSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Start -> showStart()
                is SearchState.Content -> {
                    showContent(state.data)
                    state.totalFound?.let { updateResultText(it) }
                }
                is SearchState.Loading -> showLoading()
                is SearchState.Error -> {
                    updateResultText(0)
                    showError(state.type)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilters()
    }

    private fun updateTextInputLayoutIcon(text: String) {
        if (text.isNotEmpty()) {
            setClearIcon()
        } else {
            setSearchIcon()
        }
    }

    private fun setClearIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.close_ic
        )
        binding.textInputLayout.setEndIconOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                Activity().currentFocus?.windowToken,
                0
            )
            viewModel.onClearText()
        }
    }

    private fun setSearchIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.search_ic
        )
        binding.textInputLayout.setEndIconOnClickListener {}
    }

    private fun updateResultText(count: Int) {
        binding.textResult.text = if (count > 0) {
            localeContext.resources.getQuantityString(
                R.plurals.vacamcies_found,
                count,
                count
            )
        } else {
            getString(R.string.no_vacancies_msg)
        }
    }

    private fun openVacancy(id: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            bundleOf(VACANCY_KEY to id)
        )
    }

    private fun showContent(data: PagingData<VacancyLight>) {
        adapter = VacancyAdapter { id: String -> openVacancy(id) }
        binding.recyclerViewVacancies.adapter = adapter?.withLoadStateFooter(VacancyLoadStateAdapter())
        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.submitData(data)
            binding.recyclerViewVacancies.scrollToPosition(0)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter?.loadStateFlow?.collect { loadState ->
                    (loadState.refresh == LoadState.Loading).let {
                        binding.recyclerViewVacancies.isVisible = !it
                        binding.progressBar.isVisible = it
                        binding.textResult.isVisible = !it
                    }
                }
            }
        }
        setStartVisibility(false)
        setErrorVisibility(false)
    }

    private fun showLoading() {
        setProgressVisibility(true)
        setListVisibility(false)
        setResultVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
    }

    private fun showError(errorType: ErrorType) {
        setStartVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)
        setErrorVisibility(true)

        when (errorType) {
            ErrorType.EMPTY -> {
                binding.imageInfo.setImageResource(R.drawable.empty_results_cat)
                binding.textInfo.text = getString(R.string.can_not_get_vacancies)
                setResultVisibility(true)
            }

            ErrorType.NO_CONNECTION -> {
                binding.imageInfo.setImageResource(R.drawable.skull)
                binding.textInfo.text = getString(R.string.no_internet_connection)
                setResultVisibility(false)
            }

            ErrorType.SERVER_ERROR -> {
                binding.imageInfo.setImageResource(R.drawable.search_server_error)
                binding.textInfo.text = getString(R.string.server_error)
                setResultVisibility(false)
            }
        }

    }

    private fun showStart() {
        setResultVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)
        binding.textInfo.isVisible = false
        binding.imageInfo.isVisible = true
        binding.imageInfo.setImageResource(R.drawable.search_screen_placeholder)

    }

    private fun setStartVisibility(isVisible: Boolean) {
        binding.imageInfo.isVisible = isVisible

    }

    private fun setErrorVisibility(isVisible: Boolean) {
        binding.textInfo.isVisible = isVisible
        binding.imageInfo.isVisible = isVisible

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

    private fun setFilterButtonFrame(isFraming: Boolean) {
        binding.btnFilter.setImageResource(
            if (isFraming) {
                R.drawable.filter_on__ic
            } else {
                R.drawable.filter_off_ic
            }
        )
    }
    companion object {
        const val VACANCY_KEY = "vacancy"
    }
}
