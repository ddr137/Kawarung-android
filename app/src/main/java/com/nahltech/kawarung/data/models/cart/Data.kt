package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("qty") val qty: Int,
    @SerializedName("total_discount") val total_discount: Int,
    @SerializedName("subtotal") val subTotal: Int,
    @SerializedName("orderProducts") val orderProducts: OrderProducts<Any?>,
    @SerializedName("updated_at") val updated_at: String
)