package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.ui.SalaryFormatter

class VacancyAdapter(
    private val context: Context,
    private val onItemClick: ((String) -> Unit)
) : Adapter<VacancyAdapter.ViewHolder>() {

    var vacancies: List<VacancyLight> = emptyList()
class VacancyAdapter(private val onItemClick: ((String) -> Unit)) : Adapter<VacancyAdapter.ViewHolder>() {
    private var vacancies: MutableList<VacancyLight> = mutableListOf()

    inner class ViewHolder(private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {
        private val logo = binding.imageVacancyLogo
        private val vacancyNameTextView = binding.textVacancyName
        private val vacancyEmployerTextView = binding.textVacancyEmployer
        private val vacancySalaryTextView = binding.textVacancySalary
        fun bind(vacancy: VacancyLight) {
            with(vacancy) {
                Glide.with(binding.root)
                    .load(employerLogo90)
                    .placeholder(R.drawable.placeholder_ic)
                    .into(logo)
                vacancyNameTextView.text = name
                vacancyEmployerTextView.text = employerName
                vacancySalaryTextView.text = SalaryFormatter.format(
                    context,
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

}
