package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("90") val employerLogo90: String? = null,
    @SerializedName("240") val employerLogo240: String? = null,
    @SerializedName("original") val original: String? = null
)
