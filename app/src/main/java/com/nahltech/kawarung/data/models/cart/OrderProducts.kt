package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class OrderProducts<T>(
    @SerializedName("data") val data: List<DataX>
)