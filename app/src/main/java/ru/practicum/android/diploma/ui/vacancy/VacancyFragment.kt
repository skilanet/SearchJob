package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.presentation.vacancyinfo.VacancyInfoViewModel
import ru.practicum.android.diploma.presentation.vacancyinfo.state.VacancyInfoState
import ru.practicum.android.diploma.ui.SalaryFormatter
import ru.practicum.android.diploma.ui.search.SearchFragment
import ru.practicum.android.diploma.util.BindingFragment

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {
    private val vacancyInfoViewModel: VacancyInfoViewModel by viewModel<VacancyInfoViewModel>()
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacancyInfoViewModel.getScreenStateLiveData()
            .observe(viewLifecycleOwner) {
                renderState(it)
            }
        vacancyInfoViewModel.getFavoriteButtonStateLiveData()
            .observe(viewLifecycleOwner) {
                if (it.isFavorite) {
                    binding.imageFavorite.setImageResource(R.drawable.favorites_on__ic)
                } else {
                    binding.imageFavorite.setImageResource(R.drawable.favorites_off__ic)
                }
            }
        val vacancyId = requireArguments().getString(SearchFragment.VACANCY_KEY)
        if (vacancyId == null) {
            setErrorScreenState()
        } else {
            vacancyInfoViewModel.searchVacancyInfo(vacancyId)
        }
        binding.imageFavorite.setOnClickListener {
            vacancyInfoViewModel.onFavoriteClicked()
        }
        binding.imageArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageShare.setOnClickListener {
            share()
        }
    }

    private fun resetScreenState() {
        binding.layoutContent.isVisible = false
        binding.layoutError.isVisible = false
    }

    private fun setContentScreenState(vacancy: VacancyFull) {
        resetScreenState()
        binding.layoutContent.isVisible = true
        vacancy.apply {
            with(binding) {
                textVacancyName.text = name
                textVacancySalary.text = SalaryFormatter.format(requireContext(), salaryFrom, salaryTo, salaryCurrency)
                Glide.with(requireContext())
                    .load(employerLogoOriginal ?: (employerLogo240 ?: employerLogo90))
                    .placeholder(R.drawable.placeholder_ic)
                    .into(imageEmployerLogo)
                textEmployerName.text = employerName
                textEmployerLocation.text = area
                textExperience.text = experience
                textBusyness.text = getString(R.string.busyness, employment, schedule)
                textParsedDescription.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                textKeySkillsTitle.isVisible = keySkills.isNotEmpty()
                    .also {
                        if (it) {
                            textKeySkills.text = Html.fromHtml(htmlFromList(keySkills), Html.FROM_HTML_MODE_COMPACT)
                        }
                    }
            }
        }
    }

    private fun htmlFromList(keySkills: List<String>): String {
        return keySkills.joinToString(separator = " ") { "<li> <p>$it</li> <p>" }
    }

    private fun share() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, vacancyInfoViewModel.onShareClick())
            type = "text/plain"
            startActivity(this)
        }
    }

    private fun renderState(state: VacancyInfoState) {
        when (state) {
            is VacancyInfoState.Content -> setContentScreenState(state.vacancy)
            is VacancyInfoState.Error -> setErrorScreenState()
        }
    }

    private fun setErrorScreenState() {
        resetScreenState()
        binding.layoutError.isVisible = true
    }

}
