package com.nahltech.kawarung.data.models

import com.google.gson.annotations.SerializedName

data class BuyProduct(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)