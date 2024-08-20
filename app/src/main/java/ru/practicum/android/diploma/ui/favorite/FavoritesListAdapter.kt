package ru.practicum.android.diploma.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.ui.SalaryFormatter

class FavoritesListAdapter(
    private val onItemClick: ((String) -> Unit)
) : RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>() {

    private var vacancies: MutableList<VacancyLight> = mutableListOf()

    inner class ViewHolder(private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {
        private val logo = binding.imageVacancyLogo
        private val vacancyNameTextView = binding.textVacancyName
        private val vacancyEmployerTextView = binding.textVacancyEmployer
        private val vacancySalaryTextView = binding.textVacancySalary
        fun bind(vacancy: VacancyLight) {
            with(vacancy) {
                Glide.with(binding.root)
                    .load(employerLogoOriginal ?: (employerLogo240 ?: employerLogo90))
                    .placeholder(R.drawable.placeholder_ic)
                    .into(logo)
                vacancyNameTextView.text = name
                vacancyEmployerTextView.text = employerName
                vacancySalaryTextView.text =
                    SalaryFormatter.format(
                        binding.root.context,
                        from = salaryFrom,
                        to = salaryTo,
                        currency = salaryCurrency
                    )
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
            holder.itemView.setOnClickListener { onItemClick(this.id) }
        }
    }

    fun setItems(items: List<VacancyLight>) {
        vacancies.clear()
        vacancies.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        vacancies.clear()
    }

}
