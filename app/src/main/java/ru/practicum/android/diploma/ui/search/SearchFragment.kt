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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchState
import ru.practicum.android.diploma.util.BindingFragment
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by viewModel()
    private val adapter by lazy {
        VacancyAdapter { id: String -> openVacancy(id) }
    }
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
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        setSearchIcon()
        setUpListeners()

        viewModel.observeSearchTextState().observe(viewLifecycleOwner) {
            updateTextInputLayoutIcon(it)
        }

        viewModel.observeFilterEnableState().observe(viewLifecycleOwner) {
            setFilterButtonFrame(it)
        }

        viewModel.observeSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Start -> showStart()
                is SearchState.Content -> {
                    showContent(state.data)
                }
                is SearchState.Loading -> showLoading()
                is SearchState.PageLoading -> {
                    showPageLoading()
                }
                is SearchState.Error -> {
                    updateResultText(0)
                    showError(state.type)
                }
            }
        }

        viewModel.observeTotalFoundLiveData().observe(viewLifecycleOwner) {
            updateResultText(it)
        }
    }

    private fun setUpListeners() {
        binding.editTextSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEditorActionDone()
            }
            false
        }

        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
        binding.recyclerViewVacancies.adapter = adapter
        binding.recyclerViewVacancies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerViewVacancies.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilters()
        viewModel.checkFilterApplyAndSearch()
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
        binding.textInputLayout.setEndIconOnClickListener { Unit }
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

    private fun showContent(data: List<VacancyLight>) {
        adapter.setItems(data)
        setListVisibility(true)
        setResultVisibility(true)
        setProgressVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(false)
    }

    private fun showPageLoading() {
        binding.recyclerViewVacancies.scrollToPosition(adapter.itemCount - 1)
        setListVisibility(true)
        setResultVisibility(true)
        setProgressVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(true)
    }

    private fun showLoading() {
        setProgressVisibility(true)
        setListVisibility(false)
        setResultVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(false)
    }

    private fun showError(errorType: ErrorType) {
        setStartVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)
        setErrorVisibility(true)
        setPagingProgressVisibility(false)

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
        setPagingProgressVisibility(false)
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

    private fun setPagingProgressVisibility(isVisible: Boolean) {
        binding.progressBarPaging.isVisible = isVisible
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
