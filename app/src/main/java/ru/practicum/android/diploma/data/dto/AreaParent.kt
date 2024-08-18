package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaParent(
    val id: String,
    val name: String,
    val areas: List<Area>,
    @SerializedName("parent_id") val parentId: String
)
