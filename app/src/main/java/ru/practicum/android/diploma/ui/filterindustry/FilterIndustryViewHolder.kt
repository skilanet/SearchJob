package ru.practicum.android.diploma.ui.filterindustry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding

class FilterIndustryViewHolder(private val binding: ItemIndustryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FilterIndustryAdapter.FilterItem) {
        binding.radioBtn.isChecked = item.isChecked
        binding.textIndustry.text = item.industry.name

    }
}
