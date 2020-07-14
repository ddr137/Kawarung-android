package com.nahltech.kawarung.data.models

import com.google.gson.annotations.SerializedName

data class Responses(
    @SerializedName("code") val code: Int,
    val message: String,
    val status: Int
)