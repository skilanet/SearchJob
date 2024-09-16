package ru.practicum.android.diploma.ui.filterregion

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RegionListItemBinding
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

class RegionViewHolder(private val binding: RegionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(region: AreaEntity) {
        binding.tvRegionName.text = region.name
    }
}
