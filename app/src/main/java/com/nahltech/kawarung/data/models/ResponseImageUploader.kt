package com.nahltech.kawarung.data.models

import com.google.gson.annotations.SerializedName

data class ResponseImageUploader(
    @SerializedName("image") var image: String? = null
)