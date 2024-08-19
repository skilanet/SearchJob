package ru.practicum.android.diploma.ui.filterindustry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class FilterIndustryAdapter(
    val onClick: (industry: Industry) -> Unit
) : RecyclerView.Adapter<FilterIndustryViewHolder>() {
    private val list: MutableList<FilterItem> = mutableListOf()

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
        return list.size
    }

    override fun onBindViewHolder(
        holder: FilterIndustryViewHolder,
        position: Int
    ) {
        holder.bind(list[position])
    }

    fun setList(items: List<Industry>) {
        list.clear()
        list.addAll(items.map {
            FilterItem(
                it,
                false
            )
        })
    }
}
