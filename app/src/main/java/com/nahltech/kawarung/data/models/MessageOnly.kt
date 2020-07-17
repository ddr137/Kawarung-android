package com.nahltech.kawarung.data.models

import com.google.gson.annotations.SerializedName

data class MessageOnly(
    @SerializedName("message") val message: String
)