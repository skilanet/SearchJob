package ru.practicum.android.diploma.data.dto.vacancySubClasses

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("90") var employerLogo90: String? = null,
    @SerializedName("240") var employerLogo240: String? = null,
    @SerializedName("original") var original: String? = null
)
