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

    class FilterItem(
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
        holder.bind(
            filteredList[position]
        )

        holder.itemView.setOnClickListener {
            toggle(position)
        }

    }

    private fun toggle(position: Int) {
        val item = filteredList[position]

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

                    setIsChecked(
                        filteredList,
                        checkedItem.industry.id,
                        isChecked = false,
                        toNotify = true
                    )

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

    private fun setIsChecked(
        list: List<FilterItem>,
        id: String,
        isChecked: Boolean,
        toNotify: Boolean
    ): Int {
        val pos = getItemPosition(
            list,
            id
        )

        if (pos != -1) {
            list[pos].isChecked = isChecked

            if (toNotify) {
                notifyItemChanged(pos)
            }

        }

        return pos
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

        if (unfilteredList.isNotEmpty() and (current != null)) {
            currentPos = getItemPosition(
                unfilteredList,
                current!!.id
            )
            unfilteredList[currentPos].isChecked = true

        }

        filteredList = unfilteredList.sortedBy { it.industry.name }

        notifyDataSetChanged()
    }

}
