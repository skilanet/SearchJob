package ru.practicum.android.diploma.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.favorites.state.FavoritesState
import ru.practicum.android.diploma.ui.search.SearchFragment.Companion.VACANCY_KEY
import ru.practicum.android.diploma.util.BindingFragment

class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapter by lazy {
        FavoritesListAdapter { id: String -> openVacancy(id) }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerFavorites.adapter = adapter
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.getFavorites()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun resetScreenState() {
        binding.recyclerFavorites.isVisible = false
        binding.layoutError.isVisible = false
    }

    private fun setContentScreenState(items: List<VacancyLight>) {
        resetScreenState()
        adapter.setItems(items)
        binding.recyclerFavorites.isVisible = true
    }

    private fun setErrorScreenState() {
        resetScreenState()
        binding.layoutError.isVisible = true
        binding.imageError.setImageResource(R.drawable.empty_results_cat)
        binding.textError.text = getString(R.string.can_not_get_vacancies)
    }

    private fun setEmptyScreenState() {
        resetScreenState()
        binding.layoutError.isVisible = true
        binding.imageError.setImageResource(R.drawable.empty_favorites)
        binding.textError.text = getString(R.string.the_list_is_empty)
    }

    private fun renderState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> setContentScreenState(state.vacancies)
            is FavoritesState.Empty -> setEmptyScreenState()
            is FavoritesState.Error -> setErrorScreenState()
        }
    }

    private fun openVacancy(id: String) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_vacancyFragment,
            bundleOf(VACANCY_KEY to id)
        )
    }

}
