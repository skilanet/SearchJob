package ru.practicum.android.diploma.ui.filterindustry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class FilterIndustryAdapter(
    val onClick: (industry: Industry) -> Unit
) : RecyclerView.Adapter<FilterIndustryViewHolder>() {
    private val unfilteredList: MutableList<FilterItem> = mutableListOf()
    private var filteredList: List<FilterItem> = listOf()
    private val currentFilter: String? = null

    data class FilterItem(
        val industry: Industry,
        var isChecked: Boolean
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterIndustryViewHolder {
        return FilterIndustryViewHolder(
            ItemIndustryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(
        holder: FilterIndustryViewHolder,
        position: Int
    ) {
        holder.bind(filteredList[position])
    }

    fun applyFilter(filter: String?) {
        if (currentFilter == filter) {
            return
        }

        if (filter.isNullOrEmpty()) {
            filteredList = unfilteredList.sortedBy { it.industry.name }
            notifyDataSetChanged()
            return
        }

        filteredList = unfilteredList.filter {
            it.industry.name.contains(
                filter,
                true
            )
        }.sortedBy { it.industry.name }

        notifyDataSetChanged()
    }

    fun setList(items: List<Industry>) {
        unfilteredList.clear()
        unfilteredList.addAll(items.map {
            FilterItem(
                it,
                false
            )
        })
        filteredList = unfilteredList.sortedBy { it.industry.name }
        notifyDataSetChanged()
    }

}
