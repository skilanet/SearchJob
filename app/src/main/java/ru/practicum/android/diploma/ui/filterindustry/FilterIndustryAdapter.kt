package ru.practicum.android.diploma.ui.filterindustry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class FilterIndustryAdapter(
    val onClick: (industry: Industry?) -> Unit
) : RecyclerView.Adapter<FilterIndustryViewHolder>() {
    private val unfilteredList: MutableList<FilterItem> = mutableListOf()
    private var filteredList: List<FilterItem> = listOf()
    private val currentFilter: String? = null
    private var currentPos = -1

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

    private fun getItemPosition(
        list: List<FilterItem>,
        id: String
    ): Int {
        return list.indexOfFirst {
            it.industry.id == id
        }
    }

    override fun onBindViewHolder(
        holder: FilterIndustryViewHolder,
        position: Int
    ) {
        val item = filteredList[position]
        holder.bind(
            item
        )

        holder.itemView.setOnClickListener {
            if (currentPos != position) {
                val id = item.industry.id
                val unfilteredPos = getItemPosition(
                    unfilteredList,
                    id
                )
                val unfilteredItem = unfilteredList[unfilteredPos]
                if (unfilteredItem.isChecked) {
                    currentPos = -1
                    onClick(null)
                } else {
                    val checkedIndex = unfilteredList.indexOfFirst { it.isChecked }
                    if (checkedIndex != -1) {
                        val checkedItem = unfilteredList[checkedIndex]
                        checkedItem.isChecked = false
                        val filteredPos = getItemPosition(
                            unfilteredList,
                            checkedItem.industry.id
                        )

                        filteredList[filteredPos].isChecked = false
                        notifyItemChanged(filteredPos)
                    }
                    currentPos = unfilteredPos
                    onClick(item.industry)
                }
                unfilteredItem.isChecked = !unfilteredItem.isChecked
                val filteredPos = getItemPosition(
                    filteredList,
                    unfilteredItem.industry.id
                )
                filteredList[filteredPos].isChecked = unfilteredItem.isChecked
                notifyItemChanged(filteredPos)
            }
        }

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

    fun setList(
        items: List<Industry>,
        current: Industry?
    ) {
        unfilteredList.clear()
        unfilteredList.addAll(items.map {
            FilterItem(
                it,
                false
            )
        })

        if (unfilteredList.isNotEmpty()) {
            if (current != null) {
                currentPos = getItemPosition(
                    unfilteredList,
                    current.id
                )
                unfilteredList[currentPos].isChecked = true

            }
        }

        filteredList = unfilteredList.sortedBy { it.industry.name }
        if (current != null) {
            applyFilter(current?.name)
        }

        notifyDataSetChanged()
    }

}
