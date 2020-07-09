package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("orderProducts") val orderProducts: OrderProducts<Any?>,
    @SerializedName("updated_at") val updated_at: String
)