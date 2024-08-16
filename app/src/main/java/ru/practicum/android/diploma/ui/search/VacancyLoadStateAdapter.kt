package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.databinding.LoadStateFooterBinding

class VacancyLoadStateAdapter : LoadStateAdapter<VacancyLoadStateAdapter.VacancyLoadStateViewHolder>() {
    class VacancyLoadStateViewHolder(binding: LoadStateFooterBinding) : ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VacancyLoadStateViewHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VacancyLoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyLoadStateViewHolder(binding)
    }
}
