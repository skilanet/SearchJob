package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
)
