package ru.practicum.android.diploma.data.dto.vacancySubClasses

import com.google.gson.annotations.SerializedName

data class Salary(
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("from") var from: Int? = null,
    @SerializedName("to") var to: String? = null
)
