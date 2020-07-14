package com.nahltech.kawarung.data.models.history

import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("discount") val discount: String,
    @SerializedName("discount_price") val discount_price: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("qty") val qty: String,
    @SerializedName("slug") val slug: String
)