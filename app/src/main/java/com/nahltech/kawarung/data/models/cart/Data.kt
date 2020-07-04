package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("product") val product: List<T>? = null,
    @SerializedName("updated_at") val updated_at: String
)