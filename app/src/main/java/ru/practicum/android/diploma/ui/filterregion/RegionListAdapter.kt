package ru.practicum.android.diploma.ui.filterregion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RegionListItemBinding
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

class RegionListAdapter(val onItemClick: (AreaEntity) -> Unit) : RecyclerView.Adapter<RegionViewHolder>() {
    private var items: MutableList<AreaEntity> = ArrayList()
    private val originalList: MutableList<AreaEntity> = ArrayList()
    private val filteredList: MutableList<AreaEntity> = ArrayList()

    fun setItems(items: List<AreaEntity>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
        originalList.clear()
        originalList.addAll(items)
    }

    private fun updateDisplayList(updatedList: List<AreaEntity>) {
        items.clear()
        items.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun restoreOriginal() {
        updateDisplayList(originalList)
    }

    fun filter(searchQuery: String?) {
        filteredList.clear()
        if (searchQuery.isNullOrEmpty()) {
            updateDisplayList(originalList)
        } else {
            for (item in originalList) {
                if (item.name.contains(searchQuery, true)) {
                    filteredList.add(item)
                }
            }
            updateDisplayList(filteredList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return RegionViewHolder(RegionListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClick(items[position])
        }
    }
}
