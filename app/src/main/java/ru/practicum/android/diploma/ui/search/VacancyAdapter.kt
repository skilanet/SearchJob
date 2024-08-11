package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyLight

class VacancyAdapter(private val onItemClick: ((Int) -> Unit)) : Adapter<VacancyAdapter.ViewHolder>() {

    var vacancies: List<VacancyLight> = emptyList()

    inner class ViewHolder(private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {
        private val logo = binding.imageVacancyLogo
        private val vacancyNameTextView = binding.textVacancyName
        private val vacancyEmployerTextView = binding.textVacancyEmployer
        private val vacancySalaryTextView = binding.textVacancySalary
        fun bind(vacancy: VacancyLight) {
            Glide.with(binding.root).load(vacancy.employerLogo90)
                .placeholder(R.drawable.placeholder_ic).into(logo)
            vacancyNameTextView.text = vacancy.name
            vacancyEmployerTextView.text = vacancy.employerName
            with(binding.root.context) {
                vacancySalaryTextView.text =
                    if (vacancy.salaryFrom == null && vacancy.salaryTo == null) {
                        getString(R.string.empty_salary)
                    } else if (vacancy.salaryFrom == null) {
                        getString(
                            R.string.max_salary,
                            vacancy.salaryTo,
                            vacancy.salaryCurrency
                        )
                    } else if (vacancy.salaryTo == null) {
                        getString(
                            R.string.min_salary,
                            vacancy.salaryFrom,
                            vacancy.salaryCurrency
                        )
                    } else {
                        getString(
                            R.string.full_salary,
                            vacancy.salaryFrom,
                            vacancy.salaryCurrency,
                            vacancy.salaryTo
                        )
                    }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemVacancyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = vacancies.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        with(vacancies[position]) {
            holder.bind(this)
            holder.itemView.setOnClickListener { onItemClick(this.id.toInt()) }
        }
    }
}
