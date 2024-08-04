package ru.practicum.android.diploma.data.dto.vacancySubClasses

import com.google.gson.annotations.SerializedName

data class Employer(
    @SerializedName("logo_urls") var logoUrls: LogoUrls? = LogoUrls(),
    @SerializedName("name") var name: String? = null,
)
