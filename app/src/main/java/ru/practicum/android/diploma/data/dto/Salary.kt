package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class Salary(
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("from") val from: Int? = null,
    @SerializedName("to") val to: Int? = null
)
