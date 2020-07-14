package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("data") val `data`: Data
)