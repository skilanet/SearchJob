package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class Employer(
    @SerializedName("logo_urls") val logoUrls: LogoUrls? = LogoUrls(),
    @SerializedName("name") val name: String? = null,
)
